package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IncidenteDeProveedorCriteriaTest {

    @Test
    void newIncidenteDeProveedorCriteriaHasAllFiltersNullTest() {
        var incidenteDeProveedorCriteria = new IncidenteDeProveedorCriteria();
        assertThat(incidenteDeProveedorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void incidenteDeProveedorCriteriaFluentMethodsCreatesFiltersTest() {
        var incidenteDeProveedorCriteria = new IncidenteDeProveedorCriteria();

        setAllFilters(incidenteDeProveedorCriteria);

        assertThat(incidenteDeProveedorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void incidenteDeProveedorCriteriaCopyCreatesNullFilterTest() {
        var incidenteDeProveedorCriteria = new IncidenteDeProveedorCriteria();
        var copy = incidenteDeProveedorCriteria.copy();

        assertThat(incidenteDeProveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(incidenteDeProveedorCriteria)
        );
    }

    @Test
    void incidenteDeProveedorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var incidenteDeProveedorCriteria = new IncidenteDeProveedorCriteria();
        setAllFilters(incidenteDeProveedorCriteria);

        var copy = incidenteDeProveedorCriteria.copy();

        assertThat(incidenteDeProveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(incidenteDeProveedorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var incidenteDeProveedorCriteria = new IncidenteDeProveedorCriteria();

        assertThat(incidenteDeProveedorCriteria).hasToString("IncidenteDeProveedorCriteria{}");
    }

    private static void setAllFilters(IncidenteDeProveedorCriteria incidenteDeProveedorCriteria) {
        incidenteDeProveedorCriteria.id();
        incidenteDeProveedorCriteria.ticketExterno();
        incidenteDeProveedorCriteria.estado();
        incidenteDeProveedorCriteria.abiertoEn();
        incidenteDeProveedorCriteria.primeraRespuestaEn();
        incidenteDeProveedorCriteria.resueltoEn();
        incidenteDeProveedorCriteria.cumplioSla();
        incidenteDeProveedorCriteria.motivoRechazo();
        incidenteDeProveedorCriteria.responsableResuelto();
        incidenteDeProveedorCriteria.nivelResuelto();
        incidenteDeProveedorCriteria.huboCobertura();
        incidenteDeProveedorCriteria.notas();
        incidenteDeProveedorCriteria.incidenteId();
        incidenteDeProveedorCriteria.servicioDeProveedorId();
        incidenteDeProveedorCriteria.abiertoPorId();
        incidenteDeProveedorCriteria.distinct();
    }

    private static Condition<IncidenteDeProveedorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTicketExterno()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getAbiertoEn()) &&
                condition.apply(criteria.getPrimeraRespuestaEn()) &&
                condition.apply(criteria.getResueltoEn()) &&
                condition.apply(criteria.getCumplioSla()) &&
                condition.apply(criteria.getMotivoRechazo()) &&
                condition.apply(criteria.getResponsableResuelto()) &&
                condition.apply(criteria.getNivelResuelto()) &&
                condition.apply(criteria.getHuboCobertura()) &&
                condition.apply(criteria.getNotas()) &&
                condition.apply(criteria.getIncidenteId()) &&
                condition.apply(criteria.getServicioDeProveedorId()) &&
                condition.apply(criteria.getAbiertoPorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IncidenteDeProveedorCriteria> copyFiltersAre(
        IncidenteDeProveedorCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTicketExterno(), copy.getTicketExterno()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getAbiertoEn(), copy.getAbiertoEn()) &&
                condition.apply(criteria.getPrimeraRespuestaEn(), copy.getPrimeraRespuestaEn()) &&
                condition.apply(criteria.getResueltoEn(), copy.getResueltoEn()) &&
                condition.apply(criteria.getCumplioSla(), copy.getCumplioSla()) &&
                condition.apply(criteria.getMotivoRechazo(), copy.getMotivoRechazo()) &&
                condition.apply(criteria.getResponsableResuelto(), copy.getResponsableResuelto()) &&
                condition.apply(criteria.getNivelResuelto(), copy.getNivelResuelto()) &&
                condition.apply(criteria.getHuboCobertura(), copy.getHuboCobertura()) &&
                condition.apply(criteria.getNotas(), copy.getNotas()) &&
                condition.apply(criteria.getIncidenteId(), copy.getIncidenteId()) &&
                condition.apply(criteria.getServicioDeProveedorId(), copy.getServicioDeProveedorId()) &&
                condition.apply(criteria.getAbiertoPorId(), copy.getAbiertoPorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
