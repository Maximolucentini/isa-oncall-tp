#!/usr/bin/env bash
# Verificación manual de la regla C contra la aplicación levantada (./mvnw).
# La regla vive en el backend: esto la ejercita "aunque el cliente sea curl".
#
# Uso:  bash docs/verificacion/regla-c-curl.sh | tee docs/verificacion/salida-curl.txt
# Requiere: backend en localhost:8080, usuario admin/admin, curl y node (vienen en el dev container).

set -euo pipefail
API="${API:-http://localhost:8080/api}"
SUFIJO="$(date +%s)"

# --- helpers ------------------------------------------------------------------
campo() { node -e 'let s="";process.stdin.on("data",d=>s+=d).on("end",()=>{let v=JSON.parse(s);for(const k of process.argv[1].split("."))v=v?.[k];console.log(v??"")})' "$1"; }
bonito() { node -e 'let s="";process.stdin.on("data",d=>s+=d).on("end",()=>{try{console.log(JSON.stringify(JSON.parse(s),null,2))}catch{console.log(s)}})'; }
titulo() { printf '\n==================== %s\n' "$*"; }

TOKEN="$(curl -sf -X POST "$API/authenticate" -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin"}' | campo id_token)"
AUTH=(-H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json')

crear() { curl -sf -X POST "$API/$1" "${AUTH[@]}" -d "$2" | campo id; }

pedir() { # pedir METODO RUTA [CUERPO] -> imprime código HTTP y cuerpo
  local metodo="$1" ruta="$2" cuerpo="${3:-}" salida codigo
  if [[ -n "$cuerpo" ]]; then
    salida="$(curl -s -w '\n%{http_code}' -X "$metodo" "$API$ruta" "${AUTH[@]}" -d "$cuerpo")"
  else
    salida="$(curl -s -w '\n%{http_code}' -X "$metodo" "$API$ruta" "${AUTH[@]}")"
  fi
  codigo="${salida##*$'\n'}"
  echo "$metodo $ruta  ->  HTTP $codigo"
  echo "${salida%$'\n'*}" | bonito
}

# --- escenario ----------------------------------------------------------------
titulo "Escenario"
P=$(crear proveedors "{\"nombre\":\"Nube Demo $SUFIJO\",\"tipo\":\"NUBE\",\"zonaHoraria\":\"America/New_York\",\"cobertura\":\"HORARIO_HABIL\",\"telefonoContacto\":\"+1 555 0100\",\"emailContacto\":\"soporte@nube.example\",\"urlSoporte\":\"https://nube.example/soporte\",\"slaRespuestaMinutos\":60,\"activo\":true}")
ANA=$(crear contacto-de-proveedors "{\"nombre\":\"Ana\",\"email\":\"ana@nube.example\",\"activo\":true,\"proveedor\":{\"id\":$P}}")
BETO=$(crear contacto-de-proveedors "{\"nombre\":\"Beto\",\"email\":\"beto@nube.example\",\"activo\":true,\"proveedor\":{\"id\":$P}}")
S=$(crear servicio-de-proveedors "{\"nombre\":\"Compute\",\"slaRespuestaMinutos\":15,\"activo\":true,\"proveedor\":{\"id\":$P}}")
turno() { crear turno-de-proveedors "{\"desde\":\"$2\",\"hasta\":\"$3\",\"nivel\":\"$4\",\"esReemplazo\":$5,\"contacto\":{\"id\":$1}}"; }
turno "$ANA"  2026-11-05T00:00:00Z 2026-11-05T09:00:00Z PRIMARIO   false >/dev/null
turno "$BETO" 2026-11-05T08:30:00Z 2026-11-05T17:00:00Z PRIMARIO   false >/dev/null
turno "$BETO" 2026-11-05T00:00:00Z 2026-11-06T00:00:00Z SECUNDARIO false >/dev/null
turno "$ANA"  2026-11-05T12:00:00Z 2026-11-05T13:00:00Z PRIMARIO   true  >/dev/null
echo "Proveedor=$P  Ana=$ANA  Beto=$BETO  ServicioDeProveedor=$S"
echo "Turnos (UTC): Ana P [00:00,09:00) · Beto P [08:30,17:00) · Beto S [00:00,24:00) · Ana P reemplazo [12:00,13:00)"

titulo "1. Turno simple (06:00Z): Ana primaria"
pedir GET "/oncall/proveedores/$P/guardia?instante=2026-11-05T06:00:00Z"

titulo "2. Superposición (08:45Z): gana Beto, que empezó más tarde; turnosVigentesPrimario=2"
pedir GET "/oncall/proveedores/$P/guardia?instante=2026-11-05T08:45:00Z"

titulo "3. Reemplazo (12:30Z): Ana pisa a Beto mientras dura el reemplazo"
pedir GET "/oncall/proveedores/$P/guardia?instante=2026-11-05T12:30:00Z"

titulo "4. Solo secundario (20:00Z): cubre Beto como SECUNDARIO (15:00 en Nueva York: dentro de horario hábil)"
pedir GET "/oncall/proveedores/$P/guardia?instante=2026-11-05T20:00:00Z"

titulo "5. Pidiendo nivel=PRIMARIO (20:00Z): SIN_COBERTURA, no se cae al secundario"
pedir GET "/oncall/proveedores/$P/guardia?instante=2026-11-05T20:00:00Z&nivel=PRIMARIO"

titulo "6. Hueco total (sábado 2026-11-07T12:00Z): 200 SIN_COBERTURA, contacto general, fuera de horario hábil"
pedir GET "/oncall/proveedores/$P/guardia?instante=2026-11-07T12:00:00Z"

titulo "7. Apertura de ticket ahora: congela la guardia y aplica el SLA del servicio (15)"
DESDE="$(date -u -d '-1 hour' +%Y-%m-%dT%H:%M:%SZ)"; HASTA="$(date -u -d '+1 hour' +%Y-%m-%dT%H:%M:%SZ)"
T_AHORA=$(turno "$ANA" "$DESDE" "$HASTA" PRIMARIO true)
RESP="$(curl -s -X POST "$API/oncall/incidentes-de-proveedor/abrir" "${AUTH[@]}" -d "{\"servicioDeProveedorId\":$S,\"ticketExterno\":\"CASE-$SUFIJO\"}")"
echo "$RESP" | bonito
TICKET=$(echo "$RESP" | campo ticket.id)

titulo "8. Se borra el turno de Ana y se relee el ticket: responsableResuelto no cambia"
curl -s -o /dev/null -X DELETE "$API/turno-de-proveedors/$T_AHORA" "${AUTH[@]}"
pedir GET "/incidente-de-proveedors/$TICKET"

titulo "9. Errores: proveedor inexistente (404), instante mal formado (400), servicio inexistente (404)"
pedir GET "/oncall/proveedores/999999999/guardia"
pedir GET "/oncall/proveedores/$P/guardia?instante=ayer"
pedir POST "/oncall/incidentes-de-proveedor/abrir" '{"servicioDeProveedorId":999999999}'
