package ar.edu.um.isa.oncall.service.oncall.dto;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import java.time.Instant;

/**
 * Respuesta de la regla C para un proveedor en un instante. Ver especificación 003, sección 3.2.
 */
public record ResultadoGuardia(
    Long proveedorId,
    String proveedorNombre,
    Instant instante,
    String instanteLocal,
    String zonaHorariaUsada,
    EstadoGuardia estado,
    MotivoSinCobertura motivo,
    ResponsableDeGuardia responsable,
    NivelGuardia nivelResponsable,
    ResponsableDeGuardia primario,
    ResponsableDeGuardia secundario,
    int turnosVigentesPrimario,
    int turnosVigentesSecundario,
    boolean dentroDeCoberturaContractual,
    ContactoGeneral contactoGeneral
) {
    public enum EstadoGuardia {
        CUBIERTO,
        SIN_COBERTURA,
    }

    public enum MotivoSinCobertura {
        SIN_TURNOS,
        PROVEEDOR_INACTIVO,
    }

    public record ResponsableDeGuardia(Long contactoId, String nombre, String email, String telefono, Long turnoId, boolean esReemplazo) {
        public static ResponsableDeGuardia de(TurnoDeProveedor turno) {
            ContactoDeProveedor c = turno.getContacto();
            return new ResponsableDeGuardia(
                c.getId(),
                c.getNombre(),
                c.getEmail(),
                c.getTelefono(),
                turno.getId(),
                Boolean.TRUE.equals(turno.getEsReemplazo())
            );
        }
    }

    /** Vía de contacto general del proveedor: se informa siempre, para que un hueco tenga a quién llamar. */
    public record ContactoGeneral(String telefono, String email, String urlSoporte) {}
}
