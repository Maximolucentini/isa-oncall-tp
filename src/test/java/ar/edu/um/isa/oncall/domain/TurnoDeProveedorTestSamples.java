package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TurnoDeProveedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TurnoDeProveedor getTurnoDeProveedorSample1() {
        return new TurnoDeProveedor().id(1L).nota("nota1");
    }

    public static TurnoDeProveedor getTurnoDeProveedorSample2() {
        return new TurnoDeProveedor().id(2L).nota("nota2");
    }

    public static TurnoDeProveedor getTurnoDeProveedorRandomSampleGenerator() {
        return new TurnoDeProveedor().id(longCount.incrementAndGet()).nota(UUID.randomUUID().toString());
    }
}
