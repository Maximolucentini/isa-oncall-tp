package ar.edu.um.isa.oncall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncidenteDeProveedorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncidenteDeProveedorDTO.class);
        IncidenteDeProveedorDTO incidenteDeProveedorDTO1 = new IncidenteDeProveedorDTO();
        incidenteDeProveedorDTO1.setId(1L);
        IncidenteDeProveedorDTO incidenteDeProveedorDTO2 = new IncidenteDeProveedorDTO();
        assertThat(incidenteDeProveedorDTO1).isNotEqualTo(incidenteDeProveedorDTO2);
        incidenteDeProveedorDTO2.setId(incidenteDeProveedorDTO1.getId());
        assertThat(incidenteDeProveedorDTO1).isEqualTo(incidenteDeProveedorDTO2);
        incidenteDeProveedorDTO2.setId(2L);
        assertThat(incidenteDeProveedorDTO1).isNotEqualTo(incidenteDeProveedorDTO2);
        incidenteDeProveedorDTO1.setId(null);
        assertThat(incidenteDeProveedorDTO1).isNotEqualTo(incidenteDeProveedorDTO2);
    }
}
