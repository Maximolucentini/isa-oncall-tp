package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.TurnoDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.domain.TurnoDeProveedorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnoDeProveedorMapperTest {

    private TurnoDeProveedorMapper turnoDeProveedorMapper;

    @BeforeEach
    void setUp() {
        turnoDeProveedorMapper = new TurnoDeProveedorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTurnoDeProveedorSample1();
        var actual = turnoDeProveedorMapper.toEntity(turnoDeProveedorMapper.toDto(expected));
        assertTurnoDeProveedorAllPropertiesEquals(expected, actual);
    }
}
