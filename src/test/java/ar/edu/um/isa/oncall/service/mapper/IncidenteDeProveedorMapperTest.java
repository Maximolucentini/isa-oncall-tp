package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.IncidenteDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.domain.IncidenteDeProveedorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncidenteDeProveedorMapperTest {

    private IncidenteDeProveedorMapper incidenteDeProveedorMapper;

    @BeforeEach
    void setUp() {
        incidenteDeProveedorMapper = new IncidenteDeProveedorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIncidenteDeProveedorSample1();
        var actual = incidenteDeProveedorMapper.toEntity(incidenteDeProveedorMapper.toDto(expected));
        assertIncidenteDeProveedorAllPropertiesEquals(expected, actual);
    }
}
