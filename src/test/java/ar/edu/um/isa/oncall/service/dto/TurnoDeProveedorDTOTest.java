package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnoDeProveedorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnoDeProveedorDTO.class);
        TurnoDeProveedorDTO turnoDeProveedorDTO1 = new TurnoDeProveedorDTO();
        turnoDeProveedorDTO1.setId(1L);
        TurnoDeProveedorDTO turnoDeProveedorDTO2 = new TurnoDeProveedorDTO();
        assertThat(turnoDeProveedorDTO1).isNotEqualTo(turnoDeProveedorDTO2);
        turnoDeProveedorDTO2.setId(turnoDeProveedorDTO1.getId());
        assertThat(turnoDeProveedorDTO1).isEqualTo(turnoDeProveedorDTO2);
        turnoDeProveedorDTO2.setId(2L);
        assertThat(turnoDeProveedorDTO1).isNotEqualTo(turnoDeProveedorDTO2);
        turnoDeProveedorDTO1.setId(null);
        assertThat(turnoDeProveedorDTO1).isNotEqualTo(turnoDeProveedorDTO2);
    }
}
