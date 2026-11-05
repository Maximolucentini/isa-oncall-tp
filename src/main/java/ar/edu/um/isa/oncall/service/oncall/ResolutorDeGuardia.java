package ar.edu.um.isa.oncall.service.oncall;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.CoberturaProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.ContactoGeneral;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.EstadoGuardia;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.MotivoSinCobertura;
import ar.edu.um.isa.oncall.service.oncall.dto.ResultadoGuardia.ResponsableDeGuardia;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Regla C del lado del proveedor: quién está de guardia en un instante dado.
 *
 * <p>Lógica pura: no toca la base ni lee el reloj. Recibe el proveedor, los turnos candidatos
 * y el instante, y devuelve siempre un resultado (un hueco no es una excepción).
 *
 * <p>Comportamiento: docs/especificaciones/003-guardia-proveedor.md · Decisiones: ADR 002.
 */
public class ResolutorDeGuardia {

    /** Horario hábil: lunes a viernes, [09:00, 18:00) en la zona del proveedor. */
    static final LocalTime INICIO_HABIL = LocalTime.of(9, 0);
    static final LocalTime FIN_HABIL = LocalTime.of(18, 0);

    static final ZoneId UTC = ZoneId.of("UTC");

    /** Desempate determinístico: gana el de desde más reciente; si empatan, el de id mayor. */
    private static final Comparator<TurnoDeProveedor> PRIORIDAD = Comparator.comparing(TurnoDeProveedor::getDesde).thenComparing(
        TurnoDeProveedor::getId,
        Comparator.nullsFirst(Comparator.naturalOrder())
    );

    public ResultadoGuardia resolver(
        Proveedor proveedor,
        Collection<TurnoDeProveedor> turnos,
        Instant instante,
        NivelGuardia nivelPedido,
        Severidad severidad
    ) {
        Objects.requireNonNull(proveedor, "proveedor");
        Objects.requireNonNull(instante, "instante");

        ZoneId zona = zonaDe(proveedor);
        ZonedDateTime local = instante.atZone(zona);
        boolean dentroDeContrato = dentroDeCoberturaContractual(proveedor.getCobertura(), local, severidad);
        ContactoGeneral contactoGeneral = new ContactoGeneral(
            proveedor.getTelefonoContacto(),
            proveedor.getEmailContacto(),
            proveedor.getUrlSoporte()
        );

        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            return new ResultadoGuardia(
                proveedor.getId(),
                proveedor.getNombre(),
                instante,
                local.toOffsetDateTime().toString(),
                zona.getId(),
                EstadoGuardia.SIN_COBERTURA,
                MotivoSinCobertura.PROVEEDOR_INACTIVO,
                null,
                null,
                null,
                null,
                0,
                0,
                dentroDeContrato,
                contactoGeneral
            );
        }

        List<TurnoDeProveedor> vigentes = turnos
            .stream()
            .filter(t -> esDelProveedor(t, proveedor))
            .filter(t -> esVigente(t, instante))
            .toList();
        List<TurnoDeProveedor> vigentesPrimario = delNivel(vigentes, NivelGuardia.PRIMARIO);
        List<TurnoDeProveedor> vigentesSecundario = delNivel(vigentes, NivelGuardia.SECUNDARIO);

        ResponsableDeGuardia primario = elegir(vigentesPrimario).map(ResponsableDeGuardia::de).orElse(null);
        ResponsableDeGuardia secundario = elegir(vigentesSecundario).map(ResponsableDeGuardia::de).orElse(null);

        ResponsableDeGuardia responsable;
        NivelGuardia nivelResponsable;
        if (nivelPedido == null) {
            // Sin nivel pedido: el primario, o el secundario si no hay primario.
            responsable = primario != null ? primario : secundario;
            nivelResponsable = primario != null ? NivelGuardia.PRIMARIO : secundario != null ? NivelGuardia.SECUNDARIO : null;
        } else {
            // Con nivel pedido: solo ese nivel, sin caer al otro.
            responsable = nivelPedido == NivelGuardia.PRIMARIO ? primario : secundario;
            nivelResponsable = responsable != null ? nivelPedido : null;
        }

        EstadoGuardia estado = responsable != null ? EstadoGuardia.CUBIERTO : EstadoGuardia.SIN_COBERTURA;
        return new ResultadoGuardia(
            proveedor.getId(),
            proveedor.getNombre(),
            instante,
            local.toOffsetDateTime().toString(),
            zona.getId(),
            estado,
            estado == EstadoGuardia.SIN_COBERTURA ? MotivoSinCobertura.SIN_TURNOS : null,
            responsable,
            nivelResponsable,
            primario,
            secundario,
            vigentesPrimario.size(),
            vigentesSecundario.size(),
            dentroDeContrato,
            contactoGeneral
        );
    }

    /** Intervalo semiabierto [desde, hasta). Un turno con hasta <= desde nunca es vigente. */
    static boolean esVigente(TurnoDeProveedor turno, Instant instante) {
        if (turno.getDesde() == null || turno.getHasta() == null || !turno.getHasta().isAfter(turno.getDesde())) {
            return false;
        }
        ContactoDeProveedor contacto = turno.getContacto();
        if (contacto == null || !Boolean.TRUE.equals(contacto.getActivo())) {
            return false;
        }
        return !instante.isBefore(turno.getDesde()) && instante.isBefore(turno.getHasta());
    }

    private static boolean esDelProveedor(TurnoDeProveedor turno, Proveedor proveedor) {
        ContactoDeProveedor contacto = turno.getContacto();
        if (contacto == null || contacto.getProveedor() == null) {
            return false;
        }
        return proveedor.getId() == null || Objects.equals(contacto.getProveedor().getId(), proveedor.getId());
    }

    private static List<TurnoDeProveedor> delNivel(List<TurnoDeProveedor> turnos, NivelGuardia nivel) {
        return turnos
            .stream()
            .filter(t -> t.getNivel() == nivel)
            .toList();
    }

    /** Si hay un reemplazo vigente, los turnos regulares se descartan. Después, desempate por PRIORIDAD. */
    private static Optional<TurnoDeProveedor> elegir(List<TurnoDeProveedor> vigentesDelNivel) {
        boolean hayReemplazo = vigentesDelNivel.stream().anyMatch(t -> Boolean.TRUE.equals(t.getEsReemplazo()));
        return vigentesDelNivel
            .stream()
            .filter(t -> !hayReemplazo || Boolean.TRUE.equals(t.getEsReemplazo()))
            .max(PRIORIDAD);
    }

    /** Zona IANA del proveedor; si falta o es inválida, UTC (quién está de guardia no depende de esto). */
    static ZoneId zonaDe(Proveedor proveedor) {
        String zona = proveedor.getZonaHoraria();
        if (zona == null || zona.isBlank()) {
            return UTC;
        }
        try {
            return ZoneId.of(zona.trim());
        } catch (DateTimeException e) {
            return UTC;
        }
    }

    static boolean dentroDeCoberturaContractual(CoberturaProveedor cobertura, ZonedDateTime local, Severidad severidad) {
        if (cobertura == null) {
            return true;
        }
        return switch (cobertura) {
            case VEINTICUATRO_SIETE -> true;
            case HORARIO_HABIL -> esHorarioHabil(local);
            case SOLO_CRITICO -> severidad == Severidad.SEV1;
        };
    }

    private static boolean esHorarioHabil(ZonedDateTime local) {
        DayOfWeek dia = local.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            return false;
        }
        LocalTime hora = local.toLocalTime();
        return !hora.isBefore(INICIO_HABIL) && hora.isBefore(FIN_HABIL);
    }
}
