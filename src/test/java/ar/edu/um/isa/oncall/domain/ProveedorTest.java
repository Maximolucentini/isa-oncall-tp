package ar.edu.um.isa.oncall.domain;

import static ar.edu.um.isa.oncall.domain.ContactoDeProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ProveedorTestSamples.*;
import static ar.edu.um.isa.oncall.domain.ServicioDeProveedorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.isa.oncall.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProveedorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proveedor.class);
        Proveedor proveedor1 = getProveedorSample1();
        Proveedor proveedor2 = new Proveedor();
        assertThat(proveedor1).isNotEqualTo(proveedor2);

        proveedor2.setId(proveedor1.getId());
        assertThat(proveedor1).isEqualTo(proveedor2);

        proveedor2 = getProveedorSample2();
        assertThat(proveedor1).isNotEqualTo(proveedor2);
    }

    @Test
    void servicioTest() {
        Proveedor proveedor = getProveedorRandomSampleGenerator();
        ServicioDeProveedor servicioDeProveedorBack = getServicioDeProveedorRandomSampleGenerator();

        proveedor.addServicio(servicioDeProveedorBack);
        assertThat(proveedor.getServicios()).containsOnly(servicioDeProveedorBack);
        assertThat(servicioDeProveedorBack.getProveedor()).isEqualTo(proveedor);

        proveedor.removeServicio(servicioDeProveedorBack);
        assertThat(proveedor.getServicios()).doesNotContain(servicioDeProveedorBack);
        assertThat(servicioDeProveedorBack.getProveedor()).isNull();

        proveedor.servicios(new HashSet<>(Set.of(servicioDeProveedorBack)));
        assertThat(proveedor.getServicios()).containsOnly(servicioDeProveedorBack);
        assertThat(servicioDeProveedorBack.getProveedor()).isEqualTo(proveedor);

        proveedor.setServicios(new HashSet<>());
        assertThat(proveedor.getServicios()).doesNotContain(servicioDeProveedorBack);
        assertThat(servicioDeProveedorBack.getProveedor()).isNull();
    }

    @Test
    void contactoTest() {
        Proveedor proveedor = getProveedorRandomSampleGenerator();
        ContactoDeProveedor contactoDeProveedorBack = getContactoDeProveedorRandomSampleGenerator();

        proveedor.addContacto(contactoDeProveedorBack);
        assertThat(proveedor.getContactos()).containsOnly(contactoDeProveedorBack);
        assertThat(contactoDeProveedorBack.getProveedor()).isEqualTo(proveedor);

        proveedor.removeContacto(contactoDeProveedorBack);
        assertThat(proveedor.getContactos()).doesNotContain(contactoDeProveedorBack);
        assertThat(contactoDeProveedorBack.getProveedor()).isNull();

        proveedor.contactos(new HashSet<>(Set.of(contactoDeProveedorBack)));
        assertThat(proveedor.getContactos()).containsOnly(contactoDeProveedorBack);
        assertThat(contactoDeProveedorBack.getProveedor()).isEqualTo(proveedor);

        proveedor.setContactos(new HashSet<>());
        assertThat(proveedor.getContactos()).doesNotContain(contactoDeProveedorBack);
        assertThat(contactoDeProveedorBack.getProveedor()).isNull();
    }
}
