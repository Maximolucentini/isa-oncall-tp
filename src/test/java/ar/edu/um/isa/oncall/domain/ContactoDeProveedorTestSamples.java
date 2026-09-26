package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactoDeProveedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ContactoDeProveedor getContactoDeProveedorSample1() {
        return new ContactoDeProveedor().id(1L).nombre("nombre1").email("email1").telefono("telefono1").rol("rol1");
    }

    public static ContactoDeProveedor getContactoDeProveedorSample2() {
        return new ContactoDeProveedor().id(2L).nombre("nombre2").email("email2").telefono("telefono2").rol("rol2");
    }

    public static ContactoDeProveedor getContactoDeProveedorRandomSampleGenerator() {
        return new ContactoDeProveedor()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .rol(UUID.randomUUID().toString());
    }
}
