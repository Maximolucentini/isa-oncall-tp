package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProveedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Proveedor getProveedorSample1() {
        return new Proveedor()
            .id(1L)
            .nombre("nombre1")
            .zonaHoraria("zonaHoraria1")
            .telefonoContacto("telefonoContacto1")
            .emailContacto("emailContacto1")
            .urlSoporte("urlSoporte1")
            .urlEstado("urlEstado1")
            .slaRespuestaMinutos(1);
    }

    public static Proveedor getProveedorSample2() {
        return new Proveedor()
            .id(2L)
            .nombre("nombre2")
            .zonaHoraria("zonaHoraria2")
            .telefonoContacto("telefonoContacto2")
            .emailContacto("emailContacto2")
            .urlSoporte("urlSoporte2")
            .urlEstado("urlEstado2")
            .slaRespuestaMinutos(2);
    }

    public static Proveedor getProveedorRandomSampleGenerator() {
        return new Proveedor()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .zonaHoraria(UUID.randomUUID().toString())
            .telefonoContacto(UUID.randomUUID().toString())
            .emailContacto(UUID.randomUUID().toString())
            .urlSoporte(UUID.randomUUID().toString())
            .urlEstado(UUID.randomUUID().toString())
            .slaRespuestaMinutos(intCount.incrementAndGet());
    }
}
