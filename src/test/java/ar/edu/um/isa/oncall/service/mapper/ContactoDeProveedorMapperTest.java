package ar.edu.um.isa.oncall.service.mapper;

import static ar.edu.um.isa.oncall.domain.ContactoDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.domain.ContactoDeProveedorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactoDeProveedorMapperTest {

    private ContactoDeProveedorMapper contactoDeProveedorMapper;

    @BeforeEach
    void setUp() {
        contactoDeProveedorMapper = new ContactoDeProveedorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContactoDeProveedorSample1();
        var actual = contactoDeProveedorMapper.toEntity(contactoDeProveedorMapper.toDto(expected));
        assertContactoDeProveedorAllPropertiesEquals(expected, actual);
    }
}
