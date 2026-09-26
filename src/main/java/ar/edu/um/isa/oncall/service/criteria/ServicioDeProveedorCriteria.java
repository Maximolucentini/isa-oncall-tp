package ar.edu.um.isa.oncall.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.ServicioDeProveedor} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.ServicioDeProveedorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /servicio-de-proveedors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioDeProveedorCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter identificadorExterno;

    private StringFilter descripcion;

    private IntegerFilter slaRespuestaMinutos;

    private BooleanFilter activo;

    private LongFilter proveedorId;

    private LongFilter servicioInternoId;

    private LongFilter ticketId;

    private Boolean distinct;

    public ServicioDeProveedorCriteria() {}

    public ServicioDeProveedorCriteria(ServicioDeProveedorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.identificadorExterno = other.optionalIdentificadorExterno().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.slaRespuestaMinutos = other.optionalSlaRespuestaMinutos().map(IntegerFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.proveedorId = other.optionalProveedorId().map(LongFilter::copy).orElse(null);
        this.servicioInternoId = other.optionalServicioInternoId().map(LongFilter::copy).orElse(null);
        this.ticketId = other.optionalTicketId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServicioDeProveedorCriteria copy() {
        return new ServicioDeProveedorCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getIdentificadorExterno() {
        return identificadorExterno;
    }

    public Optional<StringFilter> optionalIdentificadorExterno() {
        return Optional.ofNullable(identificadorExterno);
    }

    public StringFilter identificadorExterno() {
        if (identificadorExterno == null) {
            setIdentificadorExterno(new StringFilter());
        }
        return identificadorExterno;
    }

    public void setIdentificadorExterno(StringFilter identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public IntegerFilter getSlaRespuestaMinutos() {
        return slaRespuestaMinutos;
    }

    public Optional<IntegerFilter> optionalSlaRespuestaMinutos() {
        return Optional.ofNullable(slaRespuestaMinutos);
    }

    public IntegerFilter slaRespuestaMinutos() {
        if (slaRespuestaMinutos == null) {
            setSlaRespuestaMinutos(new IntegerFilter());
        }
        return slaRespuestaMinutos;
    }

    public void setSlaRespuestaMinutos(IntegerFilter slaRespuestaMinutos) {
        this.slaRespuestaMinutos = slaRespuestaMinutos;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
    }

    public LongFilter getProveedorId() {
        return proveedorId;
    }

    public Optional<LongFilter> optionalProveedorId() {
        return Optional.ofNullable(proveedorId);
    }

    public LongFilter proveedorId() {
        if (proveedorId == null) {
            setProveedorId(new LongFilter());
        }
        return proveedorId;
    }

    public void setProveedorId(LongFilter proveedorId) {
        this.proveedorId = proveedorId;
    }

    public LongFilter getServicioInternoId() {
        return servicioInternoId;
    }

    public Optional<LongFilter> optionalServicioInternoId() {
        return Optional.ofNullable(servicioInternoId);
    }

    public LongFilter servicioInternoId() {
        if (servicioInternoId == null) {
            setServicioInternoId(new LongFilter());
        }
        return servicioInternoId;
    }

    public void setServicioInternoId(LongFilter servicioInternoId) {
        this.servicioInternoId = servicioInternoId;
    }

    public LongFilter getTicketId() {
        return ticketId;
    }

    public Optional<LongFilter> optionalTicketId() {
        return Optional.ofNullable(ticketId);
    }

    public LongFilter ticketId() {
        if (ticketId == null) {
            setTicketId(new LongFilter());
        }
        return ticketId;
    }

    public void setTicketId(LongFilter ticketId) {
        this.ticketId = ticketId;
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
        final ServicioDeProveedorCriteria that = (ServicioDeProveedorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(identificadorExterno, that.identificadorExterno) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(slaRespuestaMinutos, that.slaRespuestaMinutos) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(proveedorId, that.proveedorId) &&
            Objects.equals(servicioInternoId, that.servicioInternoId) &&
            Objects.equals(ticketId, that.ticketId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            identificadorExterno,
            descripcion,
            slaRespuestaMinutos,
            activo,
            proveedorId,
            servicioInternoId,
            ticketId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDeProveedorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalIdentificadorExterno().map(f -> "identificadorExterno=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalSlaRespuestaMinutos().map(f -> "slaRespuestaMinutos=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalProveedorId().map(f -> "proveedorId=" + f + ", ").orElse("") +
            optionalServicioInternoId().map(f -> "servicioInternoId=" + f + ", ").orElse("") +
            optionalTicketId().map(f -> "ticketId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
