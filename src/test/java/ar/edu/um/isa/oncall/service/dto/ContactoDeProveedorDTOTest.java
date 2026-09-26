package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactoDeProveedorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactoDeProveedorDTO.class);
        ContactoDeProveedorDTO contactoDeProveedorDTO1 = new ContactoDeProveedorDTO();
        contactoDeProveedorDTO1.setId(1L);
        ContactoDeProveedorDTO contactoDeProveedorDTO2 = new ContactoDeProveedorDTO();
        assertThat(contactoDeProveedorDTO1).isNotEqualTo(contactoDeProveedorDTO2);
        contactoDeProveedorDTO2.setId(contactoDeProveedorDTO1.getId());
        assertThat(contactoDeProveedorDTO1).isEqualTo(contactoDeProveedorDTO2);
        contactoDeProveedorDTO2.setId(2L);
        assertThat(contactoDeProveedorDTO1).isNotEqualTo(contactoDeProveedorDTO2);
        contactoDeProveedorDTO1.setId(null);
        assertThat(contactoDeProveedorDTO1).isNotEqualTo(contactoDeProveedorDTO2);
    }
}
