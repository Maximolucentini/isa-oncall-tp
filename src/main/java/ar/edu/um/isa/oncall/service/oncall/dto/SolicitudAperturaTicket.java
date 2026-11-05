package ar.edu.um.isa.oncall.service.oncall.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Pedido para abrir un ticket con un proveedor aplicando la regla C. Ver especificación 003, sección 3.4.
 * abiertoEn no se acepta del cliente: lo fija el reloj del servidor.
 */
public record SolicitudAperturaTicket(
    @NotNull Long servicioDeProveedorId,
    Long incidenteId,
    @Size(max = 100) String ticketExterno,
    @Size(max = 4000) String notas
) {}
