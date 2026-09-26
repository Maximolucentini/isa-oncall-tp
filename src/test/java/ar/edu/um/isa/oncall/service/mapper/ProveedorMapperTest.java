package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ProveedorAsserts.*;
import static ar.edu.um.isa.oncall.domain.ProveedorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProveedorMapperTest {

    private ProveedorMapper proveedorMapper;

    @BeforeEach
    void setUp() {
        proveedorMapper = new ProveedorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProveedorSample1();
        var actual = proveedorMapper.toEntity(proveedorMapper.toDto(expected));
        assertProveedorAllPropertiesEquals(expected, actual);
    }
}
