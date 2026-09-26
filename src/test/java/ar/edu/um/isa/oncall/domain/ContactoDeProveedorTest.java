package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ContactoDeProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.TurnoDeProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContactoDeProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactoDeProveedor.class);
        ContactoDeProveedor contactoDeProveedor1 = getContactoDeProveedorSample1();
        ContactoDeProveedor contactoDeProveedor2 = new ContactoDeProveedor();
        assertThat(contactoDeProveedor1).isNotEqualTo(contactoDeProveedor2);

        contactoDeProveedor2.setId(contactoDeProveedor1.getId());
        assertThat(contactoDeProveedor1).isEqualTo(contactoDeProveedor2);

        contactoDeProveedor2 = getContactoDeProveedorSample2();
        assertThat(contactoDeProveedor1).isNotEqualTo(contactoDeProveedor2);
    }

    @Test
    void proveedorTest() {
        ContactoDeProveedor contactoDeProveedor = getContactoDeProveedorRandomSampleGenerator();
        Proveedor proveedorBack = getProveedorRandomSampleGenerator();

        contactoDeProveedor.setProveedor(proveedorBack);
        assertThat(contactoDeProveedor.getProveedor()).isEqualTo(proveedorBack);

        contactoDeProveedor.proveedor(null);
        assertThat(contactoDeProveedor.getProveedor()).isNull();
    }

    @Test
    void turnoTest() {
        ContactoDeProveedor contactoDeProveedor = getContactoDeProveedorRandomSampleGenerator();
        TurnoDeProveedor turnoDeProveedorBack = getTurnoDeProveedorRandomSampleGenerator();

        contactoDeProveedor.addTurno(turnoDeProveedorBack);
        assertThat(contactoDeProveedor.getTurnos()).containsOnly(turnoDeProveedorBack);
        assertThat(turnoDeProveedorBack.getContacto()).isEqualTo(contactoDeProveedor);

        contactoDeProveedor.removeTurno(turnoDeProveedorBack);
        assertThat(contactoDeProveedor.getTurnos()).doesNotContain(turnoDeProveedorBack);
        assertThat(turnoDeProveedorBack.getContacto()).isNull();

        contactoDeProveedor.turnos(new HashSet<>(Set.of(turnoDeProveedorBack)));
        assertThat(contactoDeProveedor.getTurnos()).containsOnly(turnoDeProveedorBack);
        assertThat(turnoDeProveedorBack.getContacto()).isEqualTo(contactoDeProveedor);

        contactoDeProveedor.setTurnos(new HashSet<>());
        assertThat(contactoDeProveedor.getTurnos()).doesNotContain(turnoDeProveedorBack);
        assertThat(turnoDeProveedorBack.getContacto()).isNull();
    }
}
