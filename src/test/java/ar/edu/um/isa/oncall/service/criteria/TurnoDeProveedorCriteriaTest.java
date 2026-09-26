package ar.edu.um.isa.oncall.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TurnoDeProveedorCriteriaTest {

    @Test
    void newTurnoDeProveedorCriteriaHasAllFiltersNullTest() {
        var turnoDeProveedorCriteria = new TurnoDeProveedorCriteria();
        assertThat(turnoDeProveedorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void turnoDeProveedorCriteriaFluentMethodsCreatesFiltersTest() {
        var turnoDeProveedorCriteria = new TurnoDeProveedorCriteria();

        setAllFilters(turnoDeProveedorCriteria);

        assertThat(turnoDeProveedorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void turnoDeProveedorCriteriaCopyCreatesNullFilterTest() {
        var turnoDeProveedorCriteria = new TurnoDeProveedorCriteria();
        var copy = turnoDeProveedorCriteria.copy();

        assertThat(turnoDeProveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(turnoDeProveedorCriteria)
        );
    }

    @Test
    void turnoDeProveedorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var turnoDeProveedorCriteria = new TurnoDeProveedorCriteria();
        setAllFilters(turnoDeProveedorCriteria);

        var copy = turnoDeProveedorCriteria.copy();

        assertThat(turnoDeProveedorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(turnoDeProveedorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var turnoDeProveedorCriteria = new TurnoDeProveedorCriteria();

        assertThat(turnoDeProveedorCriteria).hasToString("TurnoDeProveedorCriteria{}");
    }

    private static void setAllFilters(TurnoDeProveedorCriteria turnoDeProveedorCriteria) {
        turnoDeProveedorCriteria.id();
        turnoDeProveedorCriteria.desde();
        turnoDeProveedorCriteria.hasta();
        turnoDeProveedorCriteria.nivel();
        turnoDeProveedorCriteria.esReemplazo();
        turnoDeProveedorCriteria.nota();
        turnoDeProveedorCriteria.contactoId();
        turnoDeProveedorCriteria.distinct();
    }

    private static Condition<TurnoDeProveedorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDesde()) &&
                condition.apply(criteria.getHasta()) &&
                condition.apply(criteria.getNivel()) &&
                condition.apply(criteria.getEsReemplazo()) &&
                condition.apply(criteria.getNota()) &&
                condition.apply(criteria.getContactoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TurnoDeProveedorCriteria> copyFiltersAre(
        TurnoDeProveedorCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDesde(), copy.getDesde()) &&
                condition.apply(criteria.getHasta(), copy.getHasta()) &&
                condition.apply(criteria.getNivel(), copy.getNivel()) &&
                condition.apply(criteria.getEsReemplazo(), copy.getEsReemplazo()) &&
                condition.apply(criteria.getNota(), copy.getNota()) &&
                condition.apply(criteria.getContactoId(), copy.getContactoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
