# ADR 001 — Dónde vive el negocio

**Estado:** aceptada

## Contexto

La aplicación se regenera desde `oncall.jh`. Cada vez que cambia el modelo, `jhipster jdl oncall.jh --force` sobrescribe los archivos que el generador considera suyos: entidades, repositorios, servicios, DTOs, mappers y recursos REST. Cualquier regla escrita adentro de esos archivos se pierde en la siguiente regeneración, o obliga a resolver conflictos a mano archivo por archivo.

## Decisión

El código de negocio vive en paquetes `oncall/` que el generador no toca:

- `ar.edu.um.isa.oncall.service.oncall` — la lógica de las reglas.
- `ar.edu.um.isa.oncall.web.rest.oncall` — los endpoints que las exponen, bajo `/api/oncall/...`.

Esas clases **usan** los repositorios y servicios generados, pero no los modifican. Los nombres de paquete respetan las capas que ya verifica `TechnicalStructureTest` (`..service..` y `..web..`), así que la regla de arquitectura existente sigue cubriendo el código nuevo.

## Consecuencias

- Más clases y algo más de indirección: abrir un ticket con la regla aplicada pasa por un endpoint propio (`/api/oncall/incidentes-de-proveedor/abrir`) en lugar del `POST` generado.
- A cambio, regenerar deja de ser un evento riesgoso: el diff del generador y el diff del negocio no se mezclan.
- El CRUD generado sigue disponible y no aplica la regla. Es aceptable: es la vía de administración, no la operativa.

## Alternativas

- **Editar los servicios generados** (por ejemplo `IncidenteDeProveedorService.save`). Descartada: la próxima regeneración la borra.
- **Congelar el generador** y no regenerar nunca más. Descartada: perdemos la razón por la que usamos JHipster.
