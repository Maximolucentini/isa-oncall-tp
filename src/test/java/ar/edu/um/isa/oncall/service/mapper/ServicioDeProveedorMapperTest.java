package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ServicioDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.domain.ServicioDeProveedorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioDeProveedorMapperTest {

    private ServicioDeProveedorMapper servicioDeProveedorMapper;

    @BeforeEach
    void setUp() {
        servicioDeProveedorMapper = new ServicioDeProveedorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServicioDeProveedorSample1();
        var actual = servicioDeProveedorMapper.toEntity(servicioDeProveedorMapper.toDto(expected));
        assertServicioDeProveedorAllPropertiesEquals(expected, actual);
    }
}
