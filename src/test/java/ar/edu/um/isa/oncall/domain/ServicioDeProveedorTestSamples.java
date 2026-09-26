package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServicioDeProveedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ServicioDeProveedor getServicioDeProveedorSample1() {
        return new ServicioDeProveedor()
            .id(1L)
            .nombre("nombre1")
            .identificadorExterno("identificadorExterno1")
            .descripcion("descripcion1")
            .slaRespuestaMinutos(1);
    }

    public static ServicioDeProveedor getServicioDeProveedorSample2() {
        return new ServicioDeProveedor()
            .id(2L)
            .nombre("nombre2")
            .identificadorExterno("identificadorExterno2")
            .descripcion("descripcion2")
            .slaRespuestaMinutos(2);
    }

    public static ServicioDeProveedor getServicioDeProveedorRandomSampleGenerator() {
        return new ServicioDeProveedor()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .identificadorExterno(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .slaRespuestaMinutos(intCount.incrementAndGet());
    }
}
