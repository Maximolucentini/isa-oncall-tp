package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioDeProveedorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicioDeProveedorDTO.class);
        ServicioDeProveedorDTO servicioDeProveedorDTO1 = new ServicioDeProveedorDTO();
        servicioDeProveedorDTO1.setId(1L);
        ServicioDeProveedorDTO servicioDeProveedorDTO2 = new ServicioDeProveedorDTO();
        assertThat(servicioDeProveedorDTO1).isNotEqualTo(servicioDeProveedorDTO2);
        servicioDeProveedorDTO2.setId(servicioDeProveedorDTO1.getId());
        assertThat(servicioDeProveedorDTO1).isEqualTo(servicioDeProveedorDTO2);
        servicioDeProveedorDTO2.setId(2L);
        assertThat(servicioDeProveedorDTO1).isNotEqualTo(servicioDeProveedorDTO2);
        servicioDeProveedorDTO1.setId(null);
        assertThat(servicioDeProveedorDTO1).isNotEqualTo(servicioDeProveedorDTO2);
    }
}
