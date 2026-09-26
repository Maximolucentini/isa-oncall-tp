package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.IncidenteDeProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.IncidenteTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioDeProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncidenteDeProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncidenteDeProveedor.class);
        IncidenteDeProveedor incidenteDeProveedor1 = getIncidenteDeProveedorSample1();
        IncidenteDeProveedor incidenteDeProveedor2 = new IncidenteDeProveedor();
        assertThat(incidenteDeProveedor1).isNotEqualTo(incidenteDeProveedor2);

        incidenteDeProveedor2.setId(incidenteDeProveedor1.getId());
        assertThat(incidenteDeProveedor1).isEqualTo(incidenteDeProveedor2);

        incidenteDeProveedor2 = getIncidenteDeProveedorSample2();
        assertThat(incidenteDeProveedor1).isNotEqualTo(incidenteDeProveedor2);
    }

    @Test
    void incidenteTest() {
        IncidenteDeProveedor incidenteDeProveedor = getIncidenteDeProveedorRandomSampleGenerator();
        Incidente incidenteBack = getIncidenteRandomSampleGenerator();

        incidenteDeProveedor.setIncidente(incidenteBack);
        assertThat(incidenteDeProveedor.getIncidente()).isEqualTo(incidenteBack);

        incidenteDeProveedor.incidente(null);
        assertThat(incidenteDeProveedor.getIncidente()).isNull();
    }

    @Test
    void servicioDeProveedorTest() {
        IncidenteDeProveedor incidenteDeProveedor = getIncidenteDeProveedorRandomSampleGenerator();
        ServicioDeProveedor servicioDeProveedorBack = getServicioDeProveedorRandomSampleGenerator();

        incidenteDeProveedor.setServicioDeProveedor(servicioDeProveedorBack);
        assertThat(incidenteDeProveedor.getServicioDeProveedor()).isEqualTo(servicioDeProveedorBack);

        incidenteDeProveedor.servicioDeProveedor(null);
        assertThat(incidenteDeProveedor.getServicioDeProveedor()).isNull();
    }
}
