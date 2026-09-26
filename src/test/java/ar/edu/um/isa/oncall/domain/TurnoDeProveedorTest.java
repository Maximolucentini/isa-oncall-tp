package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ContactoDeProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.TurnoDeProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnoDeProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnoDeProveedor.class);
        TurnoDeProveedor turnoDeProveedor1 = getTurnoDeProveedorSample1();
        TurnoDeProveedor turnoDeProveedor2 = new TurnoDeProveedor();
        assertThat(turnoDeProveedor1).isNotEqualTo(turnoDeProveedor2);

        turnoDeProveedor2.setId(turnoDeProveedor1.getId());
        assertThat(turnoDeProveedor1).isEqualTo(turnoDeProveedor2);

        turnoDeProveedor2 = getTurnoDeProveedorSample2();
        assertThat(turnoDeProveedor1).isNotEqualTo(turnoDeProveedor2);
    }

    @Test
    void contactoTest() {
        TurnoDeProveedor turnoDeProveedor = getTurnoDeProveedorRandomSampleGenerator();
        ContactoDeProveedor contactoDeProveedorBack = getContactoDeProveedorRandomSampleGenerator();

        turnoDeProveedor.setContacto(contactoDeProveedorBack);
        assertThat(turnoDeProveedor.getContacto()).isEqualTo(contactoDeProveedorBack);

        turnoDeProveedor.contacto(null);
        assertThat(turnoDeProveedor.getContacto()).isNull();
    }
}
