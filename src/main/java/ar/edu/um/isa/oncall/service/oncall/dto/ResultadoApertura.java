package ar.edu.um.isa.oncall.service.oncall.dto;

import ar.edu.um.isa.oncall.service.dto.IncidenteDeProveedorDTO;

/**
 * Ticket recién abierto, junto con la guardia que se resolvió para él y el SLA que le aplica
 * (el del servicio de proveedor si está cargado; si no, el del proveedor; si no, null).
 */
public record ResultadoApertura(IncidenteDeProveedorDTO ticket, ResultadoGuardia guardia, Integer slaAplicableMinutos) {}
