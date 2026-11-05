package ar.edu.um.isa.oncall.web.rest.oncall;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.CoberturaProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoTicketProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.domain.enumeration.TipoProveedor;
import ar.edu.um.isa.oncall.repository.IncidenteDeProveedorRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Endpoints de la regla C contra una base real (Testcontainers). Cubre los criterios 10 a 12 de la
 * especificación 003 y los códigos de error; la lógica fina está en ResolutorDeGuardiaTest.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
class GuardiaProveedorResourceIT {

    private static final AtomicLong SECUENCIA = new AtomicLong(System.nanoTime());

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncidenteDeProveedorRepository incidenteDeProveedorRepository;

    private Proveedor proveedor;
    private ContactoDeProveedor ana;
    private ServicioDeProveedor servicio;
    private Instant ahora;

    @BeforeEach
    void escenario() {
        ahora = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        proveedor = new Proveedor()
            .nombre("Proveedor IT " + SECUENCIA.incrementAndGet())
            .tipo(TipoProveedor.NUBE)
            .zonaHoraria("America/New_York")
            .cobertura(CoberturaProveedor.VEINTICUATRO_SIETE)
            .telefonoContacto("+1 555 0100")
            .slaRespuestaMinutos(60)
            .activo(true);
        em.persist(proveedor);
        ana = new ContactoDeProveedor().nombre("Ana").email("ana@p.example").activo(true).proveedor(proveedor);
        em.persist(ana);
        servicio = new ServicioDeProveedor().nombre("Compute").activo(true).proveedor(proveedor);
        em.persist(servicio);
        em.flush();
    }

    private TurnoDeProveedor turnoDeAnaQueCubreAhora() {
        TurnoDeProveedor turno = new TurnoDeProveedor()
            .contacto(ana)
            .nivel(NivelGuardia.PRIMARIO)
            .esReemplazo(false)
            .desde(ahora.minus(1, ChronoUnit.HOURS))
            .hasta(ahora.plus(1, ChronoUnit.HOURS));
        em.persist(turno);
        em.flush();
        return turno;
    }

    private String solicitud(Long servicioId) {
        return "{\"servicioDeProveedorId\":" + servicioId + ",\"ticketExterno\":\"CASE-123\"}";
    }

    // ---------------------------------------------------------------- consulta

    @Test
    @DisplayName("GET guardia: devuelve al contacto de turno en el instante pedido")
    void consultaConTurno() throws Exception {
        turnoDeAnaQueCubreAhora();

        mvc.perform(get("/api/oncall/proveedores/{id}/guardia", proveedor.getId()).param("instante", ahora.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("CUBIERTO"))
            .andExpect(jsonPath("$.responsable.nombre").value("Ana"))
            .andExpect(jsonPath("$.nivelResponsable").value("PRIMARIO"));
    }

    @Test
    @DisplayName("GET guardia en un hueco: 200 con SIN_COBERTURA, no un error")
    void consultaEnHueco() throws Exception {
        mvc.perform(get("/api/oncall/proveedores/{id}/guardia", proveedor.getId()).param("instante", ahora.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("SIN_COBERTURA"))
            .andExpect(jsonPath("$.motivo").value("SIN_TURNOS"))
            .andExpect(jsonPath("$.contactoGeneral.telefono").value("+1 555 0100"));
    }

    @Test
    @DisplayName("GET guardia de un proveedor inexistente: 404")
    void consultaProveedorInexistente() throws Exception {
        mvc.perform(get("/api/oncall/proveedores/{id}/guardia", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET guardia con parámetros mal formados: 400")
    void consultaParametrosInvalidos() throws Exception {
        mvc.perform(get("/api/oncall/proveedores/{id}/guardia", proveedor.getId()).param("instante", "ayer a la tarde")).andExpect(
            status().isBadRequest()
        );
        mvc.perform(get("/api/oncall/proveedores/{id}/guardia", proveedor.getId()).param("nivel", "TERCIARIO")).andExpect(
            status().isBadRequest()
        );
    }

    // ---------------------------------------------------------------- apertura de ticket

    @Test
    @DisplayName("CA10 · la apertura congela el resultado: borrar el turno después no cambia el ticket")
    void aperturaCongelaElResultado() throws Exception {
        TurnoDeProveedor turno = turnoDeAnaQueCubreAhora();

        String respuesta = mvc
            .perform(
                post("/api/oncall/incidentes-de-proveedor/abrir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(solicitud(servicio.getId()))
            )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.ticket.estado").value("ABIERTO"))
            .andExpect(jsonPath("$.ticket.responsableResuelto").value("Ana"))
            .andExpect(jsonPath("$.ticket.nivelResuelto").value("PRIMARIO"))
            .andExpect(jsonPath("$.ticket.huboCobertura").value(true))
            .andReturn()
            .getResponse()
            .getContentAsString();
        Long ticketId = om.readTree(respuesta).at("/ticket/id").asLong();

        em.remove(em.contains(turno) ? turno : em.merge(turno));
        em.flush();
        em.clear();

        IncidenteDeProveedor ticket = incidenteDeProveedorRepository.findById(ticketId).orElseThrow();
        assertThat(ticket.getResponsableResuelto()).isEqualTo("Ana");
        assertThat(ticket.getNivelResuelto()).isEqualTo(NivelGuardia.PRIMARIO);
        assertThat(ticket.getHuboCobertura()).isTrue();
        assertThat(ticket.getEstado()).isEqualTo(EstadoTicketProveedor.ABIERTO);
        assertThat(ticket.getAbiertoEn()).isNotNull();
    }

    @Test
    @DisplayName("CA11 · apertura en un hueco: 201 con huboCobertura=false y sin responsable")
    void aperturaEnHueco() throws Exception {
        mvc.perform(
            post("/api/oncall/incidentes-de-proveedor/abrir").contentType(MediaType.APPLICATION_JSON).content(solicitud(servicio.getId()))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.ticket.huboCobertura").value(false))
            .andExpect(jsonPath("$.ticket.responsableResuelto").value(nullValue()))
            .andExpect(jsonPath("$.guardia.estado").value("SIN_COBERTURA"));
    }

    @Test
    @DisplayName("CA12 · SLA en cascada: el del servicio pisa al del proveedor; sin ninguno, null")
    void slaEnCascada() throws Exception {
        String sinSlaPropio = mvc
            .perform(
                post("/api/oncall/incidentes-de-proveedor/abrir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(solicitud(servicio.getId()))
            )
            .andReturn()
            .getResponse()
            .getContentAsString();

        servicio.setSlaRespuestaMinutos(15);
        em.flush();
        String conSlaPropio = mvc
            .perform(
                post("/api/oncall/incidentes-de-proveedor/abrir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(solicitud(servicio.getId()))
            )
            .andReturn()
            .getResponse()
            .getContentAsString();

        servicio.setSlaRespuestaMinutos(null);
        proveedor.setSlaRespuestaMinutos(null);
        em.flush();
        String sinNinguno = mvc
            .perform(
                post("/api/oncall/incidentes-de-proveedor/abrir")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(solicitud(servicio.getId()))
            )
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(sla(sinSlaPropio).asInt()).isEqualTo(60);
        assertThat(sla(conSlaPropio).asInt()).isEqualTo(15);
        assertThat(sla(sinNinguno).isNull()).isTrue();
    }

    @Test
    @DisplayName("Apertura con proveedor inactivo: 400")
    void aperturaProveedorInactivo() throws Exception {
        proveedor.setActivo(false);
        em.flush();

        mvc.perform(
            post("/api/oncall/incidentes-de-proveedor/abrir").contentType(MediaType.APPLICATION_JSON).content(solicitud(servicio.getId()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Apertura con servicio de proveedor inexistente: 404; sin servicio: 400")
    void aperturaServicioInvalido() throws Exception {
        mvc.perform(
            post("/api/oncall/incidentes-de-proveedor/abrir").contentType(MediaType.APPLICATION_JSON).content(solicitud(Long.MAX_VALUE))
        ).andExpect(status().isNotFound());
        mvc.perform(post("/api/oncall/incidentes-de-proveedor/abrir").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(
            status().isBadRequest()
        );
    }

    private JsonNode sla(String respuesta) throws Exception {
        return om.readTree(respuesta).get("slaAplicableMinutos");
    }
}
