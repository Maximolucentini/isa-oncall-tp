# Decisiones de diseño — Extensión "Proveedores externos"

**Trabajo Práctico Individual · Ingeniería de Software Aplicada · 2026**

Extensión elegida: **opción 1, Proveedores externos**, con la **Regla C** (quién está de guardia en un instante dado) aplicada del lado del proveedor. Agrega cinco entidades (`Proveedor`, `ContactoDeProveedor`, `TurnoDeProveedor`, `ServicioDeProveedor`, `IncidenteDeProveedor`) enganchadas con `Servicio`, `Incidente` y `User`. Este documento resume qué asumí donde la consigna era ambigua y por qué. El detalle está en `docs/especificaciones/003-guardia-proveedor.md` y en los ADR 001 y 002.

## Parte 1 · El modelo

**1. La guardia del proveedor es una entidad propia, no una `Rotacion`.** `Rotacion` tiene `equipo required`, y la guardia de un proveedor no tiene un equipo nuestro detrás. Reutilizarla obligaba a sacar ese `required` y agregar un vínculo opcional a `Proveedor`, y entonces una rotación podría quedar sin equipo ni proveedor, o con los dos: una combinación inválida que la base no puede impedir. Preferí no debilitar una invariante que hoy el esquema garantiza. Separé la persona (`ContactoDeProveedor`) de sus turnos (`TurnoDeProveedor`) con el mismo criterio que el modelo base aplica a `User` y `TurnoDeGuardia`.

**2. Si un estado se deduce de un campo, no es un estado.** `EstadoTicketProveedor` tiene cinco valores. Descarté "contactado" y "resolviéndose" porque ya los dicen `primeraRespuestaEn` y `resueltoEn`, y guardar el mismo dato dos veces invita a que se contradigan. `RECHAZADO` y `CANCELADO` sí quedaron porque no se pueden inferir de las fechas, y por eso existe `motivoRechazo`.

**3. Los resultados se congelan, no se recalculan.** `cumplioSla`, `responsableResuelto`, `nivelResuelto` y `huboCobertura` se persisten. Si el SLA se renegocia o alguien corrige un turno, recalcular al vuelo reescribiría el histórico contra condiciones que no regían cuando se abrió el ticket.

**4. SLA propio y en cascada.** El SLA del proveedor es lo que un tercero nos promete; `ObjetivoDeServicio` es lo que prometemos nosotros. No los mezclé. `slaRespuestaMinutos` está en `Proveedor` y, opcional, en `ServicioDeProveedor`, que lo pisa. Si ninguno está cargado (un servicio gratuito, sin contrato), `cumplioSla` queda en `null`: "no evaluable" es distinto de "no se cumplió".

**5. Sin caminos redundantes.** `TurnoDeProveedor` no apunta a `Proveedor`, porque se llega por el contacto, y un vínculo directo permitiría que los dos caminos no coincidan. `IncidenteDeProveedor → Incidente` es opcional: un proveedor puede publicar una degradación que todavía no nos afectó. `→ ServicioDeProveedor` es obligatoria, porque un ticket siempre es sobre algo que consumimos.

**6. Límites y opciones.** Seguí los del archivo base: 4000 para texto largo, 500 para URLs, 100 para nombres, y 1000 para `motivoRechazo`. Evité `TextBlob` por la incompatibilidad Hibernate/PostgreSQL que el repo base ya había corregido. Sumé las entidades nuevas a `paginate` y `filter`.

## Parte 2 · La regla C

**7. La regla vive fuera del código generado (ADR 001).** La lógica está en `service.oncall` y los endpoints en `web.rest.oncall`. No toqué `IncidenteDeProveedorService`: abrir un ticket aplicando la regla pasa por `POST /api/oncall/incidentes-de-proveedor/abrir`, y así regenerar no borra nada. El CRUD generado queda como vía de administración y no aplica la regla.

**8. El tiempo se compara en UTC; la zona horaria solo se usa para el contrato (ADR 002).** Los turnos son `Instant`, así que saber si un turno está vigente no depende de ninguna zona y el horario de verano no lo afecta. La `zonaHoraria` del proveedor se usa únicamente para evaluar `HORARIO_HABIL` (lunes a viernes de 9 a 18, en su hora local) con `ZonedDateTime`, que sí aplica el horario de verano de esa fecha. Si la zona cargada es inválida, uso UTC y lo informo en la respuesta, en lugar de fallar: quién está de guardia no depende de eso.

**9. Superposiciones y reemplazos.** Un turno cubre `[desde, hasta)`: en el instante exacto del traspaso está de guardia solo el entrante. Si dos turnos del mismo nivel se pisan, gana el que empezó más tarde, porque quien entra ya tomó la guardia; si empatan, el último cargado, para que la respuesta sea determinística. Un reemplazo pisa a cualquier turno regular aunque haya empezado antes: es la razón por la que existe. La respuesta informa cuántos turnos vigentes había en cada nivel, para que un error de carga no pase en silencio.

**10. Primario y secundario.** Cada nivel se resuelve por separado y la respuesta trae los dos. El responsable efectivo es el primario, o el secundario si no hay primario: es preferible a contestar "nadie". Si el cliente pide un nivel explícito, no lo reemplazo por el otro.

**11. Un hueco es una respuesta, no un error.** Sin turnos vigentes la regla devuelve `200` con `SIN_COBERTURA` y siempre incluye el teléfono, email y URL de soporte generales del proveedor, que es lo que se usaría en la realidad. Al abrir un ticket en un hueco, `huboCobertura` queda en `false` y `responsableResuelto` en `null`: el hueco queda registrado como dato consultable.

**12. Un turno cargado manda sobre el contrato.** Si el proveedor es `HORARIO_HABIL` o `SOLO_CRITICO` (que tomé como solo `SEV1`) pero hay un turno vigente fuera de esa franja, devuelvo a esa persona y marco `dentroDeCoberturaContractual = false`. El turno es el dato más específico; la bandera avisa que no hay compromiso del lado de ellos. Los turnos de contactos inactivos y los turnos con `hasta <= desde` se ignoran. Un proveedor inactivo responde `SIN_COBERTURA` en la consulta y rechaza la apertura de tickets.

## Verificación

- **Modelo:** el `oncall.jh` se dibuja sin errores en JDL Studio, con las cinco entidades conectadas al modelo base. `jhipster jdl oncall.jh` generó sin errores. Conservé `.devcontainer/` y el README de la cátedra ante los conflictos de sobrescritura.
- **Base y aplicación:** tras un checksum mismatch de Liquibase recreé la base (`down -v` / `up -d`) y el arranque aplicó los changelogs limpios. Backend en 8080 y frontend en 4200; con admin/admin las entidades nuevas aparecen en el menú y un `Proveedor` creado desde la interfaz persiste.
- **Regla:** _pendiente — se completa con los tests de `GuardiaProveedorServiceTest` (un test por criterio de aceptación de la especificación 003) y la prueba con `curl` de los endpoints._
