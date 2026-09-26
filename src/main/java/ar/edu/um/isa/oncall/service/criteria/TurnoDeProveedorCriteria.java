package ar.edu.um.isa.oncall.service.criteria;

import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ar.edu.um.isa.oncall.domain.TurnoDeProveedor} entity. This class is used
 * in {@link ar.edu.um.isa.oncall.web.rest.TurnoDeProveedorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /turno-de-proveedors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurnoDeProveedorCriteria implements Serializable, Criteria {

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

    private InstantFilter desde;

    private InstantFilter hasta;

    private NivelGuardiaFilter nivel;

    private BooleanFilter esReemplazo;

    private StringFilter nota;

    private LongFilter contactoId;

    private Boolean distinct;

    public TurnoDeProveedorCriteria() {}

    public TurnoDeProveedorCriteria(TurnoDeProveedorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.desde = other.optionalDesde().map(InstantFilter::copy).orElse(null);
        this.hasta = other.optionalHasta().map(InstantFilter::copy).orElse(null);
        this.nivel = other.optionalNivel().map(NivelGuardiaFilter::copy).orElse(null);
        this.esReemplazo = other.optionalEsReemplazo().map(BooleanFilter::copy).orElse(null);
        this.nota = other.optionalNota().map(StringFilter::copy).orElse(null);
        this.contactoId = other.optionalContactoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TurnoDeProveedorCriteria copy() {
        return new TurnoDeProveedorCriteria(this);
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

    public InstantFilter getDesde() {
        return desde;
    }

    public Optional<InstantFilter> optionalDesde() {
        return Optional.ofNullable(desde);
    }

    public InstantFilter desde() {
        if (desde == null) {
            setDesde(new InstantFilter());
        }
        return desde;
    }

    public void setDesde(InstantFilter desde) {
        this.desde = desde;
    }

    public InstantFilter getHasta() {
        return hasta;
    }

    public Optional<InstantFilter> optionalHasta() {
        return Optional.ofNullable(hasta);
    }

    public InstantFilter hasta() {
        if (hasta == null) {
            setHasta(new InstantFilter());
        }
        return hasta;
    }

    public void setHasta(InstantFilter hasta) {
        this.hasta = hasta;
    }

    public NivelGuardiaFilter getNivel() {
        return nivel;
    }

    public Optional<NivelGuardiaFilter> optionalNivel() {
        return Optional.ofNullable(nivel);
    }

    public NivelGuardiaFilter nivel() {
        if (nivel == null) {
            setNivel(new NivelGuardiaFilter());
        }
        return nivel;
    }

    public void setNivel(NivelGuardiaFilter nivel) {
        this.nivel = nivel;
    }

    public BooleanFilter getEsReemplazo() {
        return esReemplazo;
    }

    public Optional<BooleanFilter> optionalEsReemplazo() {
        return Optional.ofNullable(esReemplazo);
    }

    public BooleanFilter esReemplazo() {
        if (esReemplazo == null) {
            setEsReemplazo(new BooleanFilter());
        }
        return esReemplazo;
    }

    public void setEsReemplazo(BooleanFilter esReemplazo) {
        this.esReemplazo = esReemplazo;
    }

    public StringFilter getNota() {
        return nota;
    }

    public Optional<StringFilter> optionalNota() {
        return Optional.ofNullable(nota);
    }

    public StringFilter nota() {
        if (nota == null) {
            setNota(new StringFilter());
        }
        return nota;
    }

    public void setNota(StringFilter nota) {
        this.nota = nota;
    }

    public LongFilter getContactoId() {
        return contactoId;
    }

    public Optional<LongFilter> optionalContactoId() {
        return Optional.ofNullable(contactoId);
    }

    public LongFilter contactoId() {
        if (contactoId == null) {
            setContactoId(new LongFilter());
        }
        return contactoId;
    }

    public void setContactoId(LongFilter contactoId) {
        this.contactoId = contactoId;
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
        final TurnoDeProveedorCriteria that = (TurnoDeProveedorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(desde, that.desde) &&
            Objects.equals(hasta, that.hasta) &&
            Objects.equals(nivel, that.nivel) &&
            Objects.equals(esReemplazo, that.esReemplazo) &&
            Objects.equals(nota, that.nota) &&
            Objects.equals(contactoId, that.contactoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, desde, hasta, nivel, esReemplazo, nota, contactoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnoDeProveedorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDesde().map(f -> "desde=" + f + ", ").orElse("") +
            optionalHasta().map(f -> "hasta=" + f + ", ").orElse("") +
            optionalNivel().map(f -> "nivel=" + f + ", ").orElse("") +
            optionalEsReemplazo().map(f -> "esReemplazo=" + f + ", ").orElse("") +
            optionalNota().map(f -> "nota=" + f + ", ").orElse("") +
            optionalContactoId().map(f -> "contactoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
