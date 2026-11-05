# ADR 002 — Zona horaria y turnos superpuestos

**Estado:** aceptada

## Contexto

La regla C tiene que decir quién está de guardia del lado de un proveedor en un instante `t`. Dos cosas la complican:

1. **El tiempo.** Los proveedores están en otras zonas horarias, varias con horario de verano, y su cobertura contractual (`HORARIO_HABIL`) se expresa en hora local.
2. **Los turnos se pisan.** `TurnoDeProveedor` es un intervalo `desde`–`hasta` cargado a mano; nada impide que dos turnos del mismo nivel se superpongan, ni que un reemplazo se cargue encima de un turno regular.

## Decisión

**Tiempo.**
- Los turnos se guardan y se comparan como `Instant` (UTC). Saber si un turno está vigente es una comparación de instantes y **no depende de ninguna zona horaria**, por lo que el horario de verano no lo afecta.
- La `zonaHoraria` del proveedor (un id IANA, por ejemplo `America/New_York`) se usa **solo** para evaluar la cobertura contractual y para mostrar la hora local en la respuesta. Esa conversión se hace con `ZonedDateTime`, que aplica las reglas de horario de verano vigentes para esa fecha.
- Si la `zonaHoraria` cargada no es un id válido, se usa UTC y la respuesta lo indica. No se lanza un error: quién está de guardia no depende de la zona.

**Intervalos.** Un turno cubre el intervalo semiabierto `[desde, hasta)`. En el instante exacto del traspaso (`t == hasta` de uno y `t == desde` del otro) está de guardia solamente el entrante. Un turno con `hasta <= desde` es inválido y se ignora.

**Superposición, por nivel (PRIMARIO y SECUNDARIO se resuelven por separado).**
1. Si entre los turnos vigentes hay alguno con `esReemplazo = true`, se descartan los regulares: el reemplazo pisa a la rotación.
2. Entre los que quedan, gana el que **empezó más tarde** (`desde` más reciente): en un traspaso que se superpone, quien entra ya tomó la guardia.
3. Si empatan en `desde`, gana el de `id` mayor (el último cargado), para que la respuesta sea determinística.

## Consecuencias

- La misma consulta con el mismo `t` siempre devuelve la misma persona, sin importar la zona del servidor ni la del cliente.
- Un error de carga (dos turnos superpuestos) no rompe la regla, pero se resuelve en silencio. Para que se note, la respuesta informa cuántos turnos vigentes había en cada nivel.
- La cobertura contractual es informativa: no anula a una persona que tiene un turno cargado (ver especificación 003).

## Alternativas

- **Guardar los turnos en hora local del proveedor** (`LocalDateTime` más zona). Descartada: con el cambio de horario hay horas que no existen y horas que existen dos veces, y comparar dos turnos de zonas distintas obliga a convertir siempre.
- **Rechazar la superposición al cargar el turno.** Descartada por ahora: implica tocar la validación del CRUD generado (ADR 001), y los traspasos con solapamiento corto son normales en la práctica.
- **En un solapamiento, que gane el que empezó primero.** Descartada: deja de guardia a quien está terminando su turno justo cuando el entrante ya está disponible.
