# Regla C — ¿Quién está de guardia del lado del proveedor?

## 1. Contexto y objetivo

Cuando la causa de un incidente es un tercero (la nube, la pasarela de pagos, el correo), abrimos un ticket con ese proveedor. Para que el ticket sirva hay que saber **a quién** le estamos hablando: qué persona del proveedor está de guardia en ese momento y en qué nivel.

La regla responde, dado un `Proveedor` y un instante `t`, quién es el responsable. Se usa de dos formas:

- **Consulta:** `GET /api/oncall/proveedores/{id}/guardia`. Sirve para "ahora" y para cualquier instante pasado o futuro.
- **Apertura de ticket:** `POST /api/oncall/incidentes-de-proveedor/abrir`. Resuelve la guardia en el instante de apertura y **congela** el resultado en el ticket (`responsableResuelto`, `nivelResuelto`, `huboCobertura`).

## 2. Definiciones

- **Turno vigente en `t`:** un `TurnoDeProveedor` con `desde <= t < hasta` (intervalo semiabierto), cuyo `ContactoDeProveedor` está `activo` y pertenece al proveedor consultado.
- **Turno inválido:** `hasta <= desde`. Nunca es vigente.
- **Responsable de un nivel:** el turno vigente de ese nivel que queda después de aplicar las reglas de superposición (sección 3.3).
- **Responsable efectivo:** el primario si existe; si no, el secundario.
- **Cobertura contractual:** si el contrato del proveedor (`Proveedor.cobertura`) dice que debería atendernos en `t`:
  - `VEINTICUATRO_SIETE`: siempre.
  - `HORARIO_HABIL`: de lunes a viernes, de 09:00 a 18:00 (intervalo `[09:00, 18:00)`), en la `zonaHoraria` del proveedor.
  - `SOLO_CRITICO`: solo si la severidad informada es `SEV1`.
- **SLA aplicable:** `ServicioDeProveedor.slaRespuestaMinutos` si está cargado; si no, `Proveedor.slaRespuestaMinutos`; si tampoco, ninguno (`null`).

## 3. Comportamiento esperado

### 3.1 Entrada de la consulta

- `id` del proveedor (obligatorio, en la ruta).
- `instante` (opcional, ISO-8601 con zona, por ejemplo `2026-11-05T14:00:00Z`). Si falta, se usa el reloj del servidor.
- `nivel` (opcional: `PRIMARIO` o `SECUNDARIO`). Si viene, el responsable efectivo es solo el de ese nivel; no se cae al otro.
- `severidad` (opcional: `SEV1`…`SEV4`). Solo se usa para evaluar `SOLO_CRITICO`.

### 3.2 Salida

Siempre `200` con un cuerpo que tiene:

- `estado`: `CUBIERTO` o `SIN_COBERTURA`.
- `motivo` (solo si es `SIN_COBERTURA`): `SIN_TURNOS` o `PROVEEDOR_INACTIVO`.
- `responsable` y `nivelResponsable`: el responsable efectivo (nombre, email, teléfono, id de turno, si es reemplazo).
- `primario` y `secundario`: el responsable de cada nivel, o `null`.
- `turnosVigentesPrimario` y `turnosVigentesSecundario`: cuántos turnos vigentes había antes de desempatar (un número mayor a 1 indica una superposición).
- `dentroDeCoberturaContractual`: `true` o `false`.
- `instanteLocal`: `t` expresado en la zona del proveedor, y `zonaHorariaUsada`.
- `contactoGeneral`: `telefonoContacto`, `emailContacto` y `urlSoporte` del proveedor, **siempre**, para que un hueco tenga una vía de contacto.

### 3.3 Resolución de cada nivel

1. Se toman los turnos vigentes en `t` de ese nivel.
2. Si alguno es reemplazo (`esReemplazo = true`), se descartan los regulares.
3. Gana el de `desde` más reciente; si empatan, el de `id` mayor.

### 3.4 Apertura de ticket

Entrada: `servicioDeProveedorId` (obligatorio), `incidenteId` (opcional), `ticketExterno` y `notas` (opcionales).

1. `abiertoEn` = reloj del servidor. No se acepta desde el cliente.
2. Se resuelve la guardia del proveedor del servicio en `abiertoEn`, sin filtro de nivel. Si hay incidente asociado, se usa su `severidad`.
3. Se guarda el ticket con `estado = ABIERTO`, `responsableResuelto` = nombre del responsable efectivo (o `null`), `nivelResuelto` y `huboCobertura = (estado == CUBIERTO)`.
4. Responde `201` con el ticket, el resultado de la guardia y el SLA aplicable.

Cambios posteriores en los turnos **no** modifican un ticket ya abierto.

## 4. Casos borde

| #   | Caso                                                            | Respuesta                                                                                                                                                 |
| --- | --------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Ningún turno vigente                                            | `SIN_COBERTURA`, motivo `SIN_TURNOS`, `contactoGeneral` cargado. No es error.                                                                             |
| 2   | Dos turnos del mismo nivel superpuestos                         | Gana el de `desde` más reciente; el contador muestra 2.                                                                                                   |
| 3   | `t` justo en el traspaso (`t == hasta` de A, `t == desde` de B) | B.                                                                                                                                                        |
| 4   | Reemplazo encima de un turno regular                            | El reemplazo, aunque haya empezado antes.                                                                                                                 |
| 5   | Hay secundario pero no primario                                 | `CUBIERTO`, responsable = secundario, `nivelResponsable = SECUNDARIO`.                                                                                    |
| 6   | `nivel=PRIMARIO` pedido y solo hay secundario                   | `SIN_COBERTURA`, motivo `SIN_TURNOS`; `secundario` igual se informa.                                                                                      |
| 7   | Turno de un contacto con `activo = false`                       | Se ignora.                                                                                                                                                |
| 8   | Proveedor con `activo = false`                                  | Consulta: `SIN_COBERTURA`, motivo `PROVEEDOR_INACTIVO`. Apertura de ticket: `400`.                                                                        |
| 9   | Turno con `hasta <= desde`                                      | Se ignora.                                                                                                                                                |
| 10  | `HORARIO_HABIL` fuera de horario, pero con turno cargado        | `CUBIERTO` y `dentroDeCoberturaContractual = false`. El turno manda porque es el dato más específico; la bandera avisa que no hay compromiso contractual. |
| 11  | `SOLO_CRITICO` sin severidad o con severidad distinta de `SEV1` | `dentroDeCoberturaContractual = false`.                                                                                                                   |
| 12  | Cambio de horario de verano                                     | Los turnos no se ven afectados (son `Instant`). La cobertura hábil se evalúa en la hora local real de esa fecha.                                          |
| 13  | `zonaHoraria` inválida                                          | Se usa UTC y `zonaHorariaUsada = "UTC"`.                                                                                                                  |
| 14  | Proveedor inexistente                                           | `404`.                                                                                                                                                    |
| 15  | `instante`, `nivel` o `severidad` mal formados                  | `400`.                                                                                                                                                    |
| 16  | `ServicioDeProveedor` inexistente o inactivo al abrir ticket    | `404` o `400`, respectivamente.                                                                                                                           |

## 5. Criterios de aceptación

Proveedor **P** (`America/New_York`, `VEINTICUATRO_SIETE` salvo que se diga otra cosa), contactos activos **Ana** y **Beto**.

1. **Turno simple.** Ana PRIMARIO `[2026-11-05T12:00Z, 2026-11-05T20:00Z)`. Consulta en `2026-11-05T14:00Z` → `CUBIERTO`, responsable Ana, `PRIMARIO`.
2. **Hueco.** Mismo turno. Consulta en `2026-11-05T21:00Z` → `200`, `SIN_COBERTURA`, motivo `SIN_TURNOS`, `contactoGeneral` presente.
3. **Superposición.** Ana PRIMARIO `[00:00Z, 09:00Z)`, Beto PRIMARIO `[08:30Z, 17:00Z)`. Consulta en `08:45Z` → Beto, `turnosVigentesPrimario = 2`.
4. **Traspaso exacto.** Ana `[00:00Z, 09:00Z)`, Beto `[09:00Z, 17:00Z)`. Consulta en `09:00Z` → Beto.
5. **Reemplazo.** Ana PRIMARIO regular `[00:00Z, 23:00Z)`; Beto PRIMARIO reemplazo `[02:00Z, 04:00Z)`. En `03:00Z` → Beto; en `05:00Z` → Ana.
6. **Primario y secundario.** Ana PRIMARIO y Beto SECUNDARIO, ambos vigentes. Sin nivel → responsable Ana, y la respuesta trae a los dos. Con `nivel=SECUNDARIO` → Beto.
7. **Solo secundario.** Solo Beto SECUNDARIO vigente. Sin nivel → `CUBIERTO`, Beto, `SECUNDARIO`. Con `nivel=PRIMARIO` → `SIN_COBERTURA`.
8. **Horario de verano.** P con `HORARIO_HABIL`. En EE. UU. el horario de verano empieza el 8/3/2026. `2026-03-09T13:30Z` (lunes) es 09:30 EDT → `dentroDeCoberturaContractual = true`. `2026-03-02T13:30Z` (lunes) es 08:30 EST → `false`.
9. **Contacto inactivo.** El turno vigente es de un contacto con `activo = false` → `SIN_COBERTURA`.
10. **Apertura congela el resultado.** Se abre un ticket mientras Ana está de guardia: `responsableResuelto = "Ana"`, `nivelResuelto = PRIMARIO`, `huboCobertura = true`. Después se borra el turno de Ana; al releer el ticket los tres campos no cambian.
11. **Apertura en un hueco.** Se abre un ticket sin turnos vigentes → `201`, `huboCobertura = false`, `responsableResuelto = null`.
12. **SLA en cascada.** Proveedor con 60 min, servicio con 15 → SLA aplicable 15. Servicio sin SLA → 60. Ninguno → `null`.

## 6. Fuera de alcance

- Feriados del proveedor (el horario hábil no los descuenta).
- Rotaciones recurrentes generadas automáticamente: los turnos se cargan uno por uno.
- Validar superposiciones al cargar turnos (ver ADR 002).
- Calcular `cumplioSla` cuando el proveedor responde, y cambiar el estado del ticket.
- Notificar al responsable (corresponde a la regla D).

## Referencias

- Entidades: `Proveedor`, `ContactoDeProveedor`, `TurnoDeProveedor`, `ServicioDeProveedor`, `IncidenteDeProveedor`, `Incidente`.
- ADR 001 — dónde vive el negocio.
- ADR 002 — zona horaria y turnos superpuestos.
