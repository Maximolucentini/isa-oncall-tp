package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.IncidenteDeProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioDeProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServicioDeProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServicioDeProveedor.class);
        ServicioDeProveedor servicioDeProveedor1 = getServicioDeProveedorSample1();
        ServicioDeProveedor servicioDeProveedor2 = new ServicioDeProveedor();
        assertThat(servicioDeProveedor1).isNotEqualTo(servicioDeProveedor2);

        servicioDeProveedor2.setId(servicioDeProveedor1.getId());
        assertThat(servicioDeProveedor1).isEqualTo(servicioDeProveedor2);

        servicioDeProveedor2 = getServicioDeProveedorSample2();
        assertThat(servicioDeProveedor1).isNotEqualTo(servicioDeProveedor2);
    }

    @Test
    void proveedorTest() {
        ServicioDeProveedor servicioDeProveedor = getServicioDeProveedorRandomSampleGenerator();
        Proveedor proveedorBack = getProveedorRandomSampleGenerator();

        servicioDeProveedor.setProveedor(proveedorBack);
        assertThat(servicioDeProveedor.getProveedor()).isEqualTo(proveedorBack);

        servicioDeProveedor.proveedor(null);
        assertThat(servicioDeProveedor.getProveedor()).isNull();
    }

    @Test
    void servicioInternoTest() {
        ServicioDeProveedor servicioDeProveedor = getServicioDeProveedorRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        servicioDeProveedor.addServicioInterno(servicioBack);
        assertThat(servicioDeProveedor.getServicioInternos()).containsOnly(servicioBack);

        servicioDeProveedor.removeServicioInterno(servicioBack);
        assertThat(servicioDeProveedor.getServicioInternos()).doesNotContain(servicioBack);

        servicioDeProveedor.servicioInternos(new HashSet<>(Set.of(servicioBack)));
        assertThat(servicioDeProveedor.getServicioInternos()).containsOnly(servicioBack);

        servicioDeProveedor.setServicioInternos(new HashSet<>());
        assertThat(servicioDeProveedor.getServicioInternos()).doesNotContain(servicioBack);
    }

    @Test
    void ticketTest() {
        ServicioDeProveedor servicioDeProveedor = getServicioDeProveedorRandomSampleGenerator();
        IncidenteDeProveedor incidenteDeProveedorBack = getIncidenteDeProveedorRandomSampleGenerator();

        servicioDeProveedor.addTicket(incidenteDeProveedorBack);
        assertThat(servicioDeProveedor.getTickets()).containsOnly(incidenteDeProveedorBack);
        assertThat(incidenteDeProveedorBack.getServicioDeProveedor()).isEqualTo(servicioDeProveedor);

        servicioDeProveedor.removeTicket(incidenteDeProveedorBack);
        assertThat(servicioDeProveedor.getTickets()).doesNotContain(incidenteDeProveedorBack);
        assertThat(incidenteDeProveedorBack.getServicioDeProveedor()).isNull();

        servicioDeProveedor.tickets(new HashSet<>(Set.of(incidenteDeProveedorBack)));
        assertThat(servicioDeProveedor.getTickets()).containsOnly(incidenteDeProveedorBack);
        assertThat(incidenteDeProveedorBack.getServicioDeProveedor()).isEqualTo(servicioDeProveedor);

        servicioDeProveedor.setTickets(new HashSet<>());
        assertThat(servicioDeProveedor.getTickets()).doesNotContain(incidenteDeProveedorBack);
        assertThat(incidenteDeProveedorBack.getServicioDeProveedor()).isNull();
    }
}
