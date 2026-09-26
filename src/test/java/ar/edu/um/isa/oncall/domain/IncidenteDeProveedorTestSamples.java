package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IncidenteDeProveedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static IncidenteDeProveedor getIncidenteDeProveedorSample1() {
        return new IncidenteDeProveedor()
            .id(1L)
            .ticketExterno("ticketExterno1")
            .motivoRechazo("motivoRechazo1")
            .responsableResuelto("responsableResuelto1")
            .notas("notas1");
    }

    public static IncidenteDeProveedor getIncidenteDeProveedorSample2() {
        return new IncidenteDeProveedor()
            .id(2L)
            .ticketExterno("ticketExterno2")
            .motivoRechazo("motivoRechazo2")
            .responsableResuelto("responsableResuelto2")
            .notas("notas2");
    }

    public static IncidenteDeProveedor getIncidenteDeProveedorRandomSampleGenerator() {
        return new IncidenteDeProveedor()
            .id(longCount.incrementAndGet())
            .ticketExterno(UUID.randomUUID().toString())
            .motivoRechazo(UUID.randomUUID().toString())
            .responsableResuelto(UUID.randomUUID().toString())
            .notas(UUID.randomUUID().toString());
    }
}
