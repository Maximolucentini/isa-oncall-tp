package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServicioDeProveedorCriteriaTest {

    @Test
    void newServicioDeProveedorCriteriaHasAllFiltersNullTest() {
        var servicioDeProveedorCriteria = new ServicioDeProveedorCriteria();
        assertThat(servicioDeProveedorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void servicioDeProveedorCriteriaFluentMethodsCreatesFiltersTest() {
        var servicioDeProveedorCriteria = new ServicioDeProveedorCriteria();

        setAllFilters(servicioDeProveedorCriteria);

        assertThat(servicioDeProveedorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void servicioDeProveedorCriteriaCopyCreatesNullFilterTest() {
        var servicioDeProveedorCriteria = new ServicioDeProveedorCriteria();
        var copy = servicioDeProveedorCriteria.copy();

        assertThat(servicioDeProveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(servicioDeProveedorCriteria)
        );
    }

    @Test
    void servicioDeProveedorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var servicioDeProveedorCriteria = new ServicioDeProveedorCriteria();
        setAllFilters(servicioDeProveedorCriteria);

        var copy = servicioDeProveedorCriteria.copy();

        assertThat(servicioDeProveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(servicioDeProveedorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var servicioDeProveedorCriteria = new ServicioDeProveedorCriteria();

        assertThat(servicioDeProveedorCriteria).hasToString("ServicioDeProveedorCriteria{}");
    }

    private static void setAllFilters(ServicioDeProveedorCriteria servicioDeProveedorCriteria) {
        servicioDeProveedorCriteria.id();
        servicioDeProveedorCriteria.nombre();
        servicioDeProveedorCriteria.identificadorExterno();
        servicioDeProveedorCriteria.descripcion();
        servicioDeProveedorCriteria.slaRespuestaMinutos();
        servicioDeProveedorCriteria.activo();
        servicioDeProveedorCriteria.proveedorId();
        servicioDeProveedorCriteria.servicioInternoId();
        servicioDeProveedorCriteria.ticketId();
        servicioDeProveedorCriteria.distinct();
    }

    private static Condition<ServicioDeProveedorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getIdentificadorExterno()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getSlaRespuestaMinutos()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getProveedorId()) &&
                condition.apply(criteria.getServicioInternoId()) &&
                condition.apply(criteria.getTicketId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServicioDeProveedorCriteria> copyFiltersAre(
        ServicioDeProveedorCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getIdentificadorExterno(), copy.getIdentificadorExterno()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getSlaRespuestaMinutos(), copy.getSlaRespuestaMinutos()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getProveedorId(), copy.getProveedorId()) &&
                condition.apply(criteria.getServicioInternoId(), copy.getServicioInternoId()) &&
                condition.apply(criteria.getTicketId(), copy.getTicketId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
