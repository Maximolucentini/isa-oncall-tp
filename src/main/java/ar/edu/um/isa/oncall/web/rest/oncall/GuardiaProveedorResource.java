package ar.edu.um.isa.oncall.web.rest.oncall;

import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import ar.edu.um.isa.oncall.service.oncall.GuardiaProveedorService;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoApertura;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.SolicitudAperturaTicket;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de la regla C del lado del proveedor. La regla vive en el backend:
 * el resultado es el mismo si el cliente es la UI o curl.
 */
@RestController
@RequestMapping("/api/oncall")
public class GuardiaProveedorResource {

    private final GuardiaProveedorService guardiaProveedorService;

    public GuardiaProveedorResource(GuardiaProveedorService guardiaProveedorService) {
        this.guardiaProveedorService = guardiaProveedorService;
    }

    /**
     * {@code GET /api/oncall/proveedores/{id}/guardia?instante=&nivel=&severidad=}
     * Responde 200 aunque no haya nadie de guardia (estado SIN_COBERTURA), 404 si el proveedor no existe
     * y 400 si un parámetro está mal formado.
     */
    @GetMapping("/proveedores/{id}/guardia")
    public ResultadoGuardia consultarGuardia(
        @PathVariable("id") Long id,
        @RequestParam(name = "instante", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant instante,
        @RequestParam(name = "nivel", required = false) NivelGuardia nivel,
        @RequestParam(name = "severidad", required = false) Severidad severidad
    ) {
        return guardiaProveedorService.consultar(id, instante, nivel, severidad);
    }

    /**
     * {@code POST /api/oncall/incidentes-de-proveedor/abrir}
     * Abre el ticket resolviendo la guardia en el instante de apertura. 201 con el ticket, la guardia y el SLA aplicable.
     */
    @PostMapping("/incidentes-de-proveedor/abrir")
    public ResponseEntity<ResultadoApertura> abrirTicket(@Valid @RequestBody SolicitudAperturaTicket solicitud) {
        ResultadoApertura resultado = guardiaProveedorService.abrirTicket(solicitud);
        return ResponseEntity.created(URI.create("/api/incidente-de-proveedors/" + resultado.ticket().getId())).body(resultado);
    }
}
