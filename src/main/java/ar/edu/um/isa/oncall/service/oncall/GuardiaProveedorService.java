package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoTicketProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import ar.edu.um.isa.oncall.repository.IncidenteDeProveedorRepository;
import ar.edu.um.isa.oncall.repository.IncidenteRepository;
import ar.edu.um.isa.oncall.repository.ProveedorRepository;
import ar.edu.um.isa.oncall.repository.ServicioDeProveedorRepository;
import ar.edu.um.isa.oncall.repository.TurnoDeProveedorRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.security.SecurityUtils;
import ar.edu.um.isa.oncall.service.mapper.IncidenteDeProveedorMapper;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoApertura;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.EstadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.SolicitudAperturaTicket;
import jakarta.persistence.criteria.Join;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regla C del lado del proveedor, conectada a la base. La decisión de quién está de guardia
 * la toma {@link ResolutorDeGuardia}; esta clase busca los datos, fija el reloj y persiste.
 *
 * <p>Vive en service/oncall/ y no modifica ningún servicio generado (ADR 001).
 */
@Service
@Transactional(readOnly = true)
public class GuardiaProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(GuardiaProveedorService.class);

    private final ProveedorRepository proveedorRepository;
    private final TurnoDeProveedorRepository turnoDeProveedorRepository;
    private final ServicioDeProveedorRepository servicioDeProveedorRepository;
    private final IncidenteDeProveedorRepository incidenteDeProveedorRepository;
    private final IncidenteRepository incidenteRepository;
    private final UserRepository userRepository;
    private final IncidenteDeProveedorMapper incidenteDeProveedorMapper;
    private final Clock reloj;
    private final ResolutorDeGuardia resolutor = new ResolutorDeGuardia();

    public GuardiaProveedorService(
        ProveedorRepository proveedorRepository,
        TurnoDeProveedorRepository turnoDeProveedorRepository,
        ServicioDeProveedorRepository servicioDeProveedorRepository,
        IncidenteDeProveedorRepository incidenteDeProveedorRepository,
        IncidenteRepository incidenteRepository,
        UserRepository userRepository,
        IncidenteDeProveedorMapper incidenteDeProveedorMapper,
        Clock reloj
    ) {
        this.proveedorRepository = proveedorRepository;
        this.turnoDeProveedorRepository = turnoDeProveedorRepository;
        this.servicioDeProveedorRepository = servicioDeProveedorRepository;
        this.incidenteDeProveedorRepository = incidenteDeProveedorRepository;
        this.incidenteRepository = incidenteRepository;
        this.userRepository = userRepository;
        this.incidenteDeProveedorMapper = incidenteDeProveedorMapper;
        this.reloj = reloj;
    }

    /**
     * Quién está de guardia en el proveedor en {@code instante} (o ahora, si es null).
     * Siempre devuelve un resultado; un hueco viene como SIN_COBERTURA.
     */
    public ResultadoGuardia consultar(Long proveedorId, Instant instante, NivelGuardia nivel, Severidad severidad) {
        Proveedor proveedor = proveedorRepository
            .findById(proveedorId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", proveedorId));
        Instant t = instante != null ? instante : Instant.now(reloj);
        return resolutor.resolver(proveedor, turnosQueCubren(proveedorId, t), t, nivel, severidad);
    }

    /**
     * Abre un ticket con el proveedor del servicio, resolviendo la guardia en el instante de apertura
     * y congelando el resultado en el ticket (responsableResuelto, nivelResuelto, huboCobertura).
     */
    @Transactional
    public ResultadoApertura abrirTicket(SolicitudAperturaTicket solicitud) {
        ServicioDeProveedor servicio = servicioDeProveedorRepository
            .findById(solicitud.servicioDeProveedorId())
            .orElseThrow(() -> new RecursoNoEncontradoException("ServicioDeProveedor", solicitud.servicioDeProveedorId()));
        if (!Boolean.TRUE.equals(servicio.getActivo())) {
            throw new OperacionNoPermitidaException("El servicio de proveedor " + servicio.getId() + " está inactivo");
        }
        Proveedor proveedor = servicio.getProveedor();
        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            throw new OperacionNoPermitidaException("El proveedor " + proveedor.getId() + " está inactivo");
        }

        Incidente incidente = null;
        if (solicitud.incidenteId() != null) {
            incidente = incidenteRepository
                .findById(solicitud.incidenteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Incidente", solicitud.incidenteId()));
        }

        Instant ahora = Instant.now(reloj);
        Severidad severidad = incidente != null ? incidente.getSeveridad() : null;
        ResultadoGuardia guardia = resolutor.resolver(proveedor, turnosQueCubren(proveedor.getId(), ahora), ahora, null, severidad);

        IncidenteDeProveedor ticket = new IncidenteDeProveedor()
            .estado(EstadoTicketProveedor.ABIERTO)
            .abiertoEn(ahora)
            .ticketExterno(solicitud.ticketExterno())
            .notas(solicitud.notas())
            .responsableResuelto(guardia.responsable() != null ? guardia.responsable().nombre() : null)
            .nivelResuelto(guardia.nivelResponsable())
            .huboCobertura(guardia.estado() == EstadoGuardia.CUBIERTO)
            .servicioDeProveedor(servicio)
            .incidente(incidente);
        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(ticket::setAbiertoPor);

        ticket = incidenteDeProveedorRepository.save(ticket);
        LOG.info(
            "Ticket de proveedor {} abierto: proveedor={}, responsable={}, nivel={}, cobertura={}",
            ticket.getId(),
            proveedor.getNombre(),
            ticket.getResponsableResuelto(),
            ticket.getNivelResuelto(),
            ticket.getHuboCobertura()
        );
        return new ResultadoApertura(incidenteDeProveedorMapper.toDto(ticket), guardia, slaAplicable(servicio));
    }

    /** SLA en cascada: el del servicio pisa al del proveedor; si no hay ninguno, null ("no evaluable"). */
    static Integer slaAplicable(ServicioDeProveedor servicio) {
        if (servicio.getSlaRespuestaMinutos() != null) {
            return servicio.getSlaRespuestaMinutos();
        }
        return servicio.getProveedor() != null ? servicio.getProveedor().getSlaRespuestaMinutos() : null;
    }

    /**
     * Turnos de los contactos del proveedor cuyo intervalo contiene t (desde <= t < hasta).
     * El filtro fino (contacto activo, intervalo válido, reemplazos, desempate) lo hace el resolutor.
     */
    private List<TurnoDeProveedor> turnosQueCubren(Long proveedorId, Instant t) {
        Specification<TurnoDeProveedor> cubren = (root, query, cb) -> {
            Join<Object, Object> contacto = root.join("contacto");
            return cb.and(
                cb.equal(contacto.get("proveedor").get("id"), proveedorId),
                cb.lessThanOrEqualTo(root.get("desde"), t),
                cb.greaterThan(root.get("hasta"), t)
            );
        };
        return turnoDeProveedorRepository.findAll(cubren);
    }
}
