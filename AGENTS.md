# OnCall · guía para agentes

Monolito JHipster 9.2.0 — Spring Boot 4.0, Java 21, Angular 21, PostgreSQL 17.
Extensión individual: **Proveedores externos** (regla C del lado del proveedor).

## Territorios

- **GENERADO, no editar:** `domain/`, `repository/`, `service/` (sus clases directas), `service/dto/`, `service/mapper/`, `service/criteria/`, `web/rest/` (sus clases directas), `webapp/app/entities/`, `config/liquibase/changelog/`.
- **COMPARTIDO:** editar solo junto a un comentario `jhipster-needle`.
- **NUESTRO:** los paquetes `ar.edu.um.isa.oncall.service.oncall` y `ar.edu.um.isa.oncall.web.rest.oncall`, y sus tests espejo en `src/test/java/.../oncall/`. Toda regla de negocio va acá.

## Comandos

- Backend: `./mvnw` (puerto 8080) · Frontend: `npm start` (puerto 4200)
- Base: `docker compose -f src/main/docker/postgresql.yml up -d`
- Tests unitarios de la regla: `./mvnw test -Dtest=ResolutorDeGuardiaTest`
- Tests de integración de la regla (necesita Docker): `./mvnw verify -Dtest=ResolutorDeGuardiaTest -Dit.test=GuardiaProveedorResourceIT`
- Estructura de capas: `./mvnw test -Dtest=TechnicalStructureTest`
- No corras `jhipster jdl` salvo pedido explícito.

## Reglas

- El modelo se cambia en `oncall.jh`, nunca en un `.java` de `domain/`.
- No modifiques un servicio o recurso generado para meter una regla: creá una clase en `oncall/` que lo use.
- Respetá las capas de `TechnicalStructureTest`: `web` llama a `service`, `service` llama a `repository`. Nunca al revés.
- Una regla que depende del tiempo recibe el instante como parámetro. Nada de `Instant.now()` adentro de la lógica: el reloj se inyecta (`Clock`) en el borde.
- Toda regla nueva lleva su test en `src/test/java/.../oncall/`, con fechas fijas.
- Leé `docs/decisiones/` y `docs/especificaciones/` antes de proponer arquitectura o cambiar un comportamiento.
- Si un caso no está en la especificación, no lo inventes: preguntá o dejalo marcado como pendiente.
