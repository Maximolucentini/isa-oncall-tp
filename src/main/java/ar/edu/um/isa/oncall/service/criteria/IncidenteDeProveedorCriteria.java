package ar.edu.um.isa.oncall.service.criteria;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoTicketProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.IncidenteDeProveedor} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.IncidenteDeProveedorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /incidente-de-proveedors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncidenteDeProveedorCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoTicketProveedor
     */
    public static class EstadoTicketProveedorFilter extends Filter<EstadoTicketProveedor> {

        public EstadoTicketProveedorFilter() {}

        public EstadoTicketProveedorFilter(EstadoTicketProveedorFilter filter) {
            super(filter);
        }

        @Override
        public EstadoTicketProveedorFilter copy() {
            return new EstadoTicketProveedorFilter(this);
        }
    }

    /**
     * Class for filtering NivelGuardia
     */
    public static class NivelGuardiaFilter extends Filter<NivelGuardia> {

        public NivelGuardiaFilter() {}

        public NivelGuardiaFilter(NivelGuardiaFilter filter) {
            super(filter);
        }

        @Override
        public NivelGuardiaFilter copy() {
            return new NivelGuardiaFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ticketExterno;

    private EstadoTicketProveedorFilter estado;

    private InstantFilter abiertoEn;

    private InstantFilter primeraRespuestaEn;

    private InstantFilter resueltoEn;

    private BooleanFilter cumplioSla;

    private StringFilter motivoRechazo;

    private StringFilter responsableResuelto;

    private NivelGuardiaFilter nivelResuelto;

    private BooleanFilter huboCobertura;

    private StringFilter notas;

    private LongFilter incidenteId;

    private LongFilter servicioDeProveedorId;

    private LongFilter abiertoPorId;

    private Boolean distinct;

    public IncidenteDeProveedorCriteria() {}

    public IncidenteDeProveedorCriteria(IncidenteDeProveedorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ticketExterno = other.optionalTicketExterno().map(StringFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(EstadoTicketProveedorFilter::copy).orElse(null);
        this.abiertoEn = other.optionalAbiertoEn().map(InstantFilter::copy).orElse(null);
        this.primeraRespuestaEn = other.optionalPrimeraRespuestaEn().map(InstantFilter::copy).orElse(null);
        this.resueltoEn = other.optionalResueltoEn().map(InstantFilter::copy).orElse(null);
        this.cumplioSla = other.optionalCumplioSla().map(BooleanFilter::copy).orElse(null);
        this.motivoRechazo = other.optionalMotivoRechazo().map(StringFilter::copy).orElse(null);
        this.responsableResuelto = other.optionalResponsableResuelto().map(StringFilter::copy).orElse(null);
        this.nivelResuelto = other.optionalNivelResuelto().map(NivelGuardiaFilter::copy).orElse(null);
        this.huboCobertura = other.optionalHuboCobertura().map(BooleanFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.incidenteId = other.optionalIncidenteId().map(LongFilter::copy).orElse(null);
        this.servicioDeProveedorId = other.optionalServicioDeProveedorId().map(LongFilter::copy).orElse(null);
        this.abiertoPorId = other.optionalAbiertoPorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IncidenteDeProveedorCriteria copy() {
        return new IncidenteDeProveedorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTicketExterno() {
        return ticketExterno;
    }

    public Optional<StringFilter> optionalTicketExterno() {
        return Optional.ofNullable(ticketExterno);
    }

    public StringFilter ticketExterno() {
        if (ticketExterno == null) {
            setTicketExterno(new StringFilter());
        }
        return ticketExterno;
    }

    public void setTicketExterno(StringFilter ticketExterno) {
        this.ticketExterno = ticketExterno;
    }

    public EstadoTicketProveedorFilter getEstado() {
        return estado;
    }

    public Optional<EstadoTicketProveedorFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public EstadoTicketProveedorFilter estado() {
        if (estado == null) {
            setEstado(new EstadoTicketProveedorFilter());
        }
        return estado;
    }

    public void setEstado(EstadoTicketProveedorFilter estado) {
        this.estado = estado;
    }

    public InstantFilter getAbiertoEn() {
        return abiertoEn;
    }

    public Optional<InstantFilter> optionalAbiertoEn() {
        return Optional.ofNullable(abiertoEn);
    }

    public InstantFilter abiertoEn() {
        if (abiertoEn == null) {
            setAbiertoEn(new InstantFilter());
        }
        return abiertoEn;
    }

    public void setAbiertoEn(InstantFilter abiertoEn) {
        this.abiertoEn = abiertoEn;
    }

    public InstantFilter getPrimeraRespuestaEn() {
        return primeraRespuestaEn;
    }

    public Optional<InstantFilter> optionalPrimeraRespuestaEn() {
        return Optional.ofNullable(primeraRespuestaEn);
    }

    public InstantFilter primeraRespuestaEn() {
        if (primeraRespuestaEn == null) {
            setPrimeraRespuestaEn(new InstantFilter());
        }
        return primeraRespuestaEn;
    }

    public void setPrimeraRespuestaEn(InstantFilter primeraRespuestaEn) {
        this.primeraRespuestaEn = primeraRespuestaEn;
    }

    public InstantFilter getResueltoEn() {
        return resueltoEn;
    }

    public Optional<InstantFilter> optionalResueltoEn() {
        return Optional.ofNullable(resueltoEn);
    }

    public InstantFilter resueltoEn() {
        if (resueltoEn == null) {
            setResueltoEn(new InstantFilter());
        }
        return resueltoEn;
    }

    public void setResueltoEn(InstantFilter resueltoEn) {
        this.resueltoEn = resueltoEn;
    }

    public BooleanFilter getCumplioSla() {
        return cumplioSla;
    }

    public Optional<BooleanFilter> optionalCumplioSla() {
        return Optional.ofNullable(cumplioSla);
    }

    public BooleanFilter cumplioSla() {
        if (cumplioSla == null) {
            setCumplioSla(new BooleanFilter());
        }
        return cumplioSla;
    }

    public void setCumplioSla(BooleanFilter cumplioSla) {
        this.cumplioSla = cumplioSla;
    }

    public StringFilter getMotivoRechazo() {
        return motivoRechazo;
    }

    public Optional<StringFilter> optionalMotivoRechazo() {
        return Optional.ofNullable(motivoRechazo);
    }

    public StringFilter motivoRechazo() {
        if (motivoRechazo == null) {
            setMotivoRechazo(new StringFilter());
        }
        return motivoRechazo;
    }

    public void setMotivoRechazo(StringFilter motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public StringFilter getResponsableResuelto() {
        return responsableResuelto;
    }

    public Optional<StringFilter> optionalResponsableResuelto() {
        return Optional.ofNullable(responsableResuelto);
    }

    public StringFilter responsableResuelto() {
        if (responsableResuelto == null) {
            setResponsableResuelto(new StringFilter());
        }
        return responsableResuelto;
    }

    public void setResponsableResuelto(StringFilter responsableResuelto) {
        this.responsableResuelto = responsableResuelto;
    }

    public NivelGuardiaFilter getNivelResuelto() {
        return nivelResuelto;
    }

    public Optional<NivelGuardiaFilter> optionalNivelResuelto() {
        return Optional.ofNullable(nivelResuelto);
    }

    public NivelGuardiaFilter nivelResuelto() {
        if (nivelResuelto == null) {
            setNivelResuelto(new NivelGuardiaFilter());
        }
        return nivelResuelto;
    }

    public void setNivelResuelto(NivelGuardiaFilter nivelResuelto) {
        this.nivelResuelto = nivelResuelto;
    }

    public BooleanFilter getHuboCobertura() {
        return huboCobertura;
    }

    public Optional<BooleanFilter> optionalHuboCobertura() {
        return Optional.ofNullable(huboCobertura);
    }

    public BooleanFilter huboCobertura() {
        if (huboCobertura == null) {
            setHuboCobertura(new BooleanFilter());
        }
        return huboCobertura;
    }

    public void setHuboCobertura(BooleanFilter huboCobertura) {
        this.huboCobertura = huboCobertura;
    }

    public StringFilter getNotas() {
        return notas;
    }

    public Optional<StringFilter> optionalNotas() {
        return Optional.ofNullable(notas);
    }

    public StringFilter notas() {
        if (notas == null) {
            setNotas(new StringFilter());
        }
        return notas;
    }

    public void setNotas(StringFilter notas) {
        this.notas = notas;
    }

    public LongFilter getIncidenteId() {
        return incidenteId;
    }

    public Optional<LongFilter> optionalIncidenteId() {
        return Optional.ofNullable(incidenteId);
    }

    public LongFilter incidenteId() {
        if (incidenteId == null) {
            setIncidenteId(new LongFilter());
        }
        return incidenteId;
    }

    public void setIncidenteId(LongFilter incidenteId) {
        this.incidenteId = incidenteId;
    }

    public LongFilter getServicioDeProveedorId() {
        return servicioDeProveedorId;
    }

    public Optional<LongFilter> optionalServicioDeProveedorId() {
        return Optional.ofNullable(servicioDeProveedorId);
    }

    public LongFilter servicioDeProveedorId() {
        if (servicioDeProveedorId == null) {
            setServicioDeProveedorId(new LongFilter());
        }
        return servicioDeProveedorId;
    }

    public void setServicioDeProveedorId(LongFilter servicioDeProveedorId) {
        this.servicioDeProveedorId = servicioDeProveedorId;
    }

    public LongFilter getAbiertoPorId() {
        return abiertoPorId;
    }

    public Optional<LongFilter> optionalAbiertoPorId() {
        return Optional.ofNullable(abiertoPorId);
    }

    public LongFilter abiertoPorId() {
        if (abiertoPorId == null) {
            setAbiertoPorId(new LongFilter());
        }
        return abiertoPorId;
    }

    public void setAbiertoPorId(LongFilter abiertoPorId) {
        this.abiertoPorId = abiertoPorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IncidenteDeProveedorCriteria that = (IncidenteDeProveedorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ticketExterno, that.ticketExterno) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(abiertoEn, that.abiertoEn) &&
            Objects.equals(primeraRespuestaEn, that.primeraRespuestaEn) &&
            Objects.equals(resueltoEn, that.resueltoEn) &&
            Objects.equals(cumplioSla, that.cumplioSla) &&
            Objects.equals(motivoRechazo, that.motivoRechazo) &&
            Objects.equals(responsableResuelto, that.responsableResuelto) &&
            Objects.equals(nivelResuelto, that.nivelResuelto) &&
            Objects.equals(huboCobertura, that.huboCobertura) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(incidenteId, that.incidenteId) &&
            Objects.equals(servicioDeProveedorId, that.servicioDeProveedorId) &&
            Objects.equals(abiertoPorId, that.abiertoPorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            ticketExterno,
            estado,
            abiertoEn,
            primeraRespuestaEn,
            resueltoEn,
            cumplioSla,
            motivoRechazo,
            responsableResuelto,
            nivelResuelto,
            huboCobertura,
            notas,
            incidenteId,
            servicioDeProveedorId,
            abiertoPorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncidenteDeProveedorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTicketExterno().map(f -> "ticketExterno=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalAbiertoEn().map(f -> "abiertoEn=" + f + ", ").orElse("") +
            optionalPrimeraRespuestaEn().map(f -> "primeraRespuestaEn=" + f + ", ").orElse("") +
            optionalResueltoEn().map(f -> "resueltoEn=" + f + ", ").orElse("") +
            optionalCumplioSla().map(f -> "cumplioSla=" + f + ", ").orElse("") +
            optionalMotivoRechazo().map(f -> "motivoRechazo=" + f + ", ").orElse("") +
            optionalResponsableResuelto().map(f -> "responsableResuelto=" + f + ", ").orElse("") +
            optionalNivelResuelto().map(f -> "nivelResuelto=" + f + ", ").orElse("") +
            optionalHuboCobertura().map(f -> "huboCobertura=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalIncidenteId().map(f -> "incidenteId=" + f + ", ").orElse("") +
            optionalServicioDeProveedorId().map(f -> "servicioDeProveedorId=" + f + ", ").orElse("") +
            optionalAbiertoPorId().map(f -> "abiertoPorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
