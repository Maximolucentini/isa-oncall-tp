package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.IncidenteDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoTicketProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.repository.IncidenteDeProveedorRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.IncidenteDeProveedorService;
import ar.edu.um.isa.oncall.service.dto.IncidenteDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.IncidenteDeProveedorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IncidenteDeProveedorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IncidenteDeProveedorResourceIT {

    private static final String DEFAULT_TICKET_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_EXTERNO = "BBBBBBBBBB";

    private static final EstadoTicketProveedor DEFAULT_ESTADO = EstadoTicketProveedor.ABIERTO;
    private static final EstadoTicketProveedor UPDATED_ESTADO = EstadoTicketProveedor.ESPERANDO_PROVEEDOR;

    private static final Instant DEFAULT_ABIERTO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ABIERTO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_PRIMERA_RESPUESTA_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRIMERA_RESPUESTA_EN = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_RESUELTO_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESUELTO_EN = Instant.ofEpochMilli(1701729143509L);

    private static final Boolean DEFAULT_CUMPLIO_SLA = false;
    private static final Boolean UPDATED_CUMPLIO_SLA = true;

    private static final String DEFAULT_MOTIVO_RECHAZO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_RECHAZO = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE_RESUELTO = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE_RESUELTO = "BBBBBBBBBB";

    private static final NivelGuardia DEFAULT_NIVEL_RESUELTO = NivelGuardia.PRIMARIO;
    private static final NivelGuardia UPDATED_NIVEL_RESUELTO = NivelGuardia.SECUNDARIO;

    private static final Boolean DEFAULT_HUBO_COBERTURA = false;
    private static final Boolean UPDATED_HUBO_COBERTURA = true;

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/incidente-de-proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncidenteDeProveedorRepository incidenteDeProveedorRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private IncidenteDeProveedorRepository incidenteDeProveedorRepositoryMock;

    @Autowired
    private IncidenteDeProveedorMapper incidenteDeProveedorMapper;

    @Mock
    private IncidenteDeProveedorService incidenteDeProveedorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncidenteDeProveedorMockMvc;

    private IncidenteDeProveedor incidenteDeProveedor;

    private IncidenteDeProveedor insertedIncidenteDeProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncidenteDeProveedor createEntity(EntityManager em) {
        IncidenteDeProveedor incidenteDeProveedor = new IncidenteDeProveedor()
            .ticketExterno(DEFAULT_TICKET_EXTERNO)
            .estado(DEFAULT_ESTADO)
            .abiertoEn(DEFAULT_ABIERTO_EN)
            .primeraRespuestaEn(DEFAULT_PRIMERA_RESPUESTA_EN)
            .resueltoEn(DEFAULT_RESUELTO_EN)
            .cumplioSla(DEFAULT_CUMPLIO_SLA)
            .motivoRechazo(DEFAULT_MOTIVO_RECHAZO)
            .responsableResuelto(DEFAULT_RESPONSABLE_RESUELTO)
            .nivelResuelto(DEFAULT_NIVEL_RESUELTO)
            .huboCobertura(DEFAULT_HUBO_COBERTURA)
            .notas(DEFAULT_NOTAS);
        // Add required entity
        ServicioDeProveedor servicioDeProveedor;
        if (TestUtil.findAll(em, ServicioDeProveedor.class).isEmpty()) {
            servicioDeProveedor = ServicioDeProveedorResourceIT.createEntity(em);
            em.persist(servicioDeProveedor);
            em.flush();
        } else {
            servicioDeProveedor = TestUtil.findAll(em, ServicioDeProveedor.class).get(0);
        }
        incidenteDeProveedor.setServicioDeProveedor(servicioDeProveedor);
        return incidenteDeProveedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncidenteDeProveedor createUpdatedEntity(EntityManager em) {
        IncidenteDeProveedor updatedIncidenteDeProveedor = new IncidenteDeProveedor()
            .ticketExterno(UPDATED_TICKET_EXTERNO)
            .estado(UPDATED_ESTADO)
            .abiertoEn(UPDATED_ABIERTO_EN)
            .primeraRespuestaEn(UPDATED_PRIMERA_RESPUESTA_EN)
            .resueltoEn(UPDATED_RESUELTO_EN)
            .cumplioSla(UPDATED_CUMPLIO_SLA)
            .motivoRechazo(UPDATED_MOTIVO_RECHAZO)
            .responsableResuelto(UPDATED_RESPONSABLE_RESUELTO)
            .nivelResuelto(UPDATED_NIVEL_RESUELTO)
            .huboCobertura(UPDATED_HUBO_COBERTURA)
            .notas(UPDATED_NOTAS);
        // Add required entity
        ServicioDeProveedor servicioDeProveedor;
        if (TestUtil.findAll(em, ServicioDeProveedor.class).isEmpty()) {
            servicioDeProveedor = ServicioDeProveedorResourceIT.createUpdatedEntity(em);
            em.persist(servicioDeProveedor);
            em.flush();
        } else {
            servicioDeProveedor = TestUtil.findAll(em, ServicioDeProveedor.class).get(0);
        }
        updatedIncidenteDeProveedor.setServicioDeProveedor(servicioDeProveedor);
        return updatedIncidenteDeProveedor;
    }

    @BeforeEach
    void initTest() {
        incidenteDeProveedor = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedIncidenteDeProveedor != null) {
            incidenteDeProveedorRepository.delete(insertedIncidenteDeProveedor);
            insertedIncidenteDeProveedor = null;
        }
    }

    @Test
    @Transactional
    void createIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);
        var returnedIncidenteDeProveedorDTO = om.readValue(
            restIncidenteDeProveedorMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidenteDeProveedorDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IncidenteDeProveedorDTO.class
        );

        // Validate the IncidenteDeProveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIncidenteDeProveedor = incidenteDeProveedorMapper.toEntity(returnedIncidenteDeProveedorDTO);
        assertIncidenteDeProveedorUpdatableFieldsEquals(
            returnedIncidenteDeProveedor,
            getPersistedIncidenteDeProveedor(returnedIncidenteDeProveedor)
        );

        insertedIncidenteDeProveedor = returnedIncidenteDeProveedor;
    }

    @Test
    @Transactional
    void createIncidenteDeProveedorWithExistingId() throws Exception {
        // Create the IncidenteDeProveedor with an existing ID
        incidenteDeProveedor.setId(1L);
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncidenteDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidenteDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incidenteDeProveedor.setEstado(null);

        // Create the IncidenteDeProveedor, which fails.
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        restIncidenteDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidenteDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAbiertoEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        incidenteDeProveedor.setAbiertoEn(null);

        // Create the IncidenteDeProveedor, which fails.
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        restIncidenteDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidenteDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedors() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList
        restIncidenteDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incidenteDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticketExterno").value(hasItem(DEFAULT_TICKET_EXTERNO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].abiertoEn").value(hasItem(DEFAULT_ABIERTO_EN.toString())))
            .andExpect(jsonPath("$.[*].primeraRespuestaEn").value(hasItem(DEFAULT_PRIMERA_RESPUESTA_EN.toString())))
            .andExpect(jsonPath("$.[*].resueltoEn").value(hasItem(DEFAULT_RESUELTO_EN.toString())))
            .andExpect(jsonPath("$.[*].cumplioSla").value(hasItem(DEFAULT_CUMPLIO_SLA)))
            .andExpect(jsonPath("$.[*].motivoRechazo").value(hasItem(DEFAULT_MOTIVO_RECHAZO)))
            .andExpect(jsonPath("$.[*].responsableResuelto").value(hasItem(DEFAULT_RESPONSABLE_RESUELTO)))
            .andExpect(jsonPath("$.[*].nivelResuelto").value(hasItem(DEFAULT_NIVEL_RESUELTO.toString())))
            .andExpect(jsonPath("$.[*].huboCobertura").value(hasItem(DEFAULT_HUBO_COBERTURA)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIncidenteDeProveedorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(incidenteDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIncidenteDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(incidenteDeProveedorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIncidenteDeProveedorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(incidenteDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIncidenteDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(incidenteDeProveedorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIncidenteDeProveedor() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get the incidenteDeProveedor
        restIncidenteDeProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, incidenteDeProveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incidenteDeProveedor.getId().intValue()))
            .andExpect(jsonPath("$.ticketExterno").value(DEFAULT_TICKET_EXTERNO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.abiertoEn").value(DEFAULT_ABIERTO_EN.toString()))
            .andExpect(jsonPath("$.primeraRespuestaEn").value(DEFAULT_PRIMERA_RESPUESTA_EN.toString()))
            .andExpect(jsonPath("$.resueltoEn").value(DEFAULT_RESUELTO_EN.toString()))
            .andExpect(jsonPath("$.cumplioSla").value(DEFAULT_CUMPLIO_SLA))
            .andExpect(jsonPath("$.motivoRechazo").value(DEFAULT_MOTIVO_RECHAZO))
            .andExpect(jsonPath("$.responsableResuelto").value(DEFAULT_RESPONSABLE_RESUELTO))
            .andExpect(jsonPath("$.nivelResuelto").value(DEFAULT_NIVEL_RESUELTO.toString()))
            .andExpect(jsonPath("$.huboCobertura").value(DEFAULT_HUBO_COBERTURA))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getIncidenteDeProveedorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        Long id = incidenteDeProveedor.getId();

        defaultIncidenteDeProveedorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIncidenteDeProveedorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIncidenteDeProveedorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByTicketExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where ticketExterno equals to
        defaultIncidenteDeProveedorFiltering(
            "ticketExterno.equals=" + DEFAULT_TICKET_EXTERNO,
            "ticketExterno.equals=" + UPDATED_TICKET_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByTicketExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where ticketExterno in
        defaultIncidenteDeProveedorFiltering(
            "ticketExterno.in=" + DEFAULT_TICKET_EXTERNO + "," + UPDATED_TICKET_EXTERNO,
            "ticketExterno.in=" + UPDATED_TICKET_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByTicketExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where ticketExterno is not null
        defaultIncidenteDeProveedorFiltering("ticketExterno.specified=true", "ticketExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByTicketExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where ticketExterno contains
        defaultIncidenteDeProveedorFiltering(
            "ticketExterno.contains=" + DEFAULT_TICKET_EXTERNO,
            "ticketExterno.contains=" + UPDATED_TICKET_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByTicketExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where ticketExterno does not contain
        defaultIncidenteDeProveedorFiltering(
            "ticketExterno.doesNotContain=" + UPDATED_TICKET_EXTERNO,
            "ticketExterno.doesNotContain=" + DEFAULT_TICKET_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where estado equals to
        defaultIncidenteDeProveedorFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where estado in
        defaultIncidenteDeProveedorFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where estado is not null
        defaultIncidenteDeProveedorFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByAbiertoEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where abiertoEn equals to
        defaultIncidenteDeProveedorFiltering("abiertoEn.equals=" + DEFAULT_ABIERTO_EN, "abiertoEn.equals=" + UPDATED_ABIERTO_EN);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByAbiertoEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where abiertoEn in
        defaultIncidenteDeProveedorFiltering(
            "abiertoEn.in=" + DEFAULT_ABIERTO_EN + "," + UPDATED_ABIERTO_EN,
            "abiertoEn.in=" + UPDATED_ABIERTO_EN
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByAbiertoEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where abiertoEn is not null
        defaultIncidenteDeProveedorFiltering("abiertoEn.specified=true", "abiertoEn.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByPrimeraRespuestaEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where primeraRespuestaEn equals to
        defaultIncidenteDeProveedorFiltering(
            "primeraRespuestaEn.equals=" + DEFAULT_PRIMERA_RESPUESTA_EN,
            "primeraRespuestaEn.equals=" + UPDATED_PRIMERA_RESPUESTA_EN
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByPrimeraRespuestaEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where primeraRespuestaEn in
        defaultIncidenteDeProveedorFiltering(
            "primeraRespuestaEn.in=" + DEFAULT_PRIMERA_RESPUESTA_EN + "," + UPDATED_PRIMERA_RESPUESTA_EN,
            "primeraRespuestaEn.in=" + UPDATED_PRIMERA_RESPUESTA_EN
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByPrimeraRespuestaEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where primeraRespuestaEn is not null
        defaultIncidenteDeProveedorFiltering("primeraRespuestaEn.specified=true", "primeraRespuestaEn.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResueltoEnIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where resueltoEn equals to
        defaultIncidenteDeProveedorFiltering("resueltoEn.equals=" + DEFAULT_RESUELTO_EN, "resueltoEn.equals=" + UPDATED_RESUELTO_EN);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResueltoEnIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where resueltoEn in
        defaultIncidenteDeProveedorFiltering(
            "resueltoEn.in=" + DEFAULT_RESUELTO_EN + "," + UPDATED_RESUELTO_EN,
            "resueltoEn.in=" + UPDATED_RESUELTO_EN
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResueltoEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where resueltoEn is not null
        defaultIncidenteDeProveedorFiltering("resueltoEn.specified=true", "resueltoEn.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByCumplioSlaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where cumplioSla equals to
        defaultIncidenteDeProveedorFiltering("cumplioSla.equals=" + DEFAULT_CUMPLIO_SLA, "cumplioSla.equals=" + UPDATED_CUMPLIO_SLA);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByCumplioSlaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where cumplioSla in
        defaultIncidenteDeProveedorFiltering(
            "cumplioSla.in=" + DEFAULT_CUMPLIO_SLA + "," + UPDATED_CUMPLIO_SLA,
            "cumplioSla.in=" + UPDATED_CUMPLIO_SLA
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByCumplioSlaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where cumplioSla is not null
        defaultIncidenteDeProveedorFiltering("cumplioSla.specified=true", "cumplioSla.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByMotivoRechazoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where motivoRechazo equals to
        defaultIncidenteDeProveedorFiltering(
            "motivoRechazo.equals=" + DEFAULT_MOTIVO_RECHAZO,
            "motivoRechazo.equals=" + UPDATED_MOTIVO_RECHAZO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByMotivoRechazoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where motivoRechazo in
        defaultIncidenteDeProveedorFiltering(
            "motivoRechazo.in=" + DEFAULT_MOTIVO_RECHAZO + "," + UPDATED_MOTIVO_RECHAZO,
            "motivoRechazo.in=" + UPDATED_MOTIVO_RECHAZO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByMotivoRechazoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where motivoRechazo is not null
        defaultIncidenteDeProveedorFiltering("motivoRechazo.specified=true", "motivoRechazo.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByMotivoRechazoContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where motivoRechazo contains
        defaultIncidenteDeProveedorFiltering(
            "motivoRechazo.contains=" + DEFAULT_MOTIVO_RECHAZO,
            "motivoRechazo.contains=" + UPDATED_MOTIVO_RECHAZO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByMotivoRechazoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where motivoRechazo does not contain
        defaultIncidenteDeProveedorFiltering(
            "motivoRechazo.doesNotContain=" + UPDATED_MOTIVO_RECHAZO,
            "motivoRechazo.doesNotContain=" + DEFAULT_MOTIVO_RECHAZO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResponsableResueltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where responsableResuelto equals to
        defaultIncidenteDeProveedorFiltering(
            "responsableResuelto.equals=" + DEFAULT_RESPONSABLE_RESUELTO,
            "responsableResuelto.equals=" + UPDATED_RESPONSABLE_RESUELTO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResponsableResueltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where responsableResuelto in
        defaultIncidenteDeProveedorFiltering(
            "responsableResuelto.in=" + DEFAULT_RESPONSABLE_RESUELTO + "," + UPDATED_RESPONSABLE_RESUELTO,
            "responsableResuelto.in=" + UPDATED_RESPONSABLE_RESUELTO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResponsableResueltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where responsableResuelto is not null
        defaultIncidenteDeProveedorFiltering("responsableResuelto.specified=true", "responsableResuelto.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResponsableResueltoContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where responsableResuelto contains
        defaultIncidenteDeProveedorFiltering(
            "responsableResuelto.contains=" + DEFAULT_RESPONSABLE_RESUELTO,
            "responsableResuelto.contains=" + UPDATED_RESPONSABLE_RESUELTO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByResponsableResueltoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where responsableResuelto does not contain
        defaultIncidenteDeProveedorFiltering(
            "responsableResuelto.doesNotContain=" + UPDATED_RESPONSABLE_RESUELTO,
            "responsableResuelto.doesNotContain=" + DEFAULT_RESPONSABLE_RESUELTO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNivelResueltoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where nivelResuelto equals to
        defaultIncidenteDeProveedorFiltering(
            "nivelResuelto.equals=" + DEFAULT_NIVEL_RESUELTO,
            "nivelResuelto.equals=" + UPDATED_NIVEL_RESUELTO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNivelResueltoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where nivelResuelto in
        defaultIncidenteDeProveedorFiltering(
            "nivelResuelto.in=" + DEFAULT_NIVEL_RESUELTO + "," + UPDATED_NIVEL_RESUELTO,
            "nivelResuelto.in=" + UPDATED_NIVEL_RESUELTO
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNivelResueltoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where nivelResuelto is not null
        defaultIncidenteDeProveedorFiltering("nivelResuelto.specified=true", "nivelResuelto.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByHuboCoberturaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where huboCobertura equals to
        defaultIncidenteDeProveedorFiltering(
            "huboCobertura.equals=" + DEFAULT_HUBO_COBERTURA,
            "huboCobertura.equals=" + UPDATED_HUBO_COBERTURA
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByHuboCoberturaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where huboCobertura in
        defaultIncidenteDeProveedorFiltering(
            "huboCobertura.in=" + DEFAULT_HUBO_COBERTURA + "," + UPDATED_HUBO_COBERTURA,
            "huboCobertura.in=" + UPDATED_HUBO_COBERTURA
        );
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByHuboCoberturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where huboCobertura is not null
        defaultIncidenteDeProveedorFiltering("huboCobertura.specified=true", "huboCobertura.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNotasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where notas equals to
        defaultIncidenteDeProveedorFiltering("notas.equals=" + DEFAULT_NOTAS, "notas.equals=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNotasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where notas in
        defaultIncidenteDeProveedorFiltering("notas.in=" + DEFAULT_NOTAS + "," + UPDATED_NOTAS, "notas.in=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNotasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where notas is not null
        defaultIncidenteDeProveedorFiltering("notas.specified=true", "notas.specified=false");
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNotasContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where notas contains
        defaultIncidenteDeProveedorFiltering("notas.contains=" + DEFAULT_NOTAS, "notas.contains=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByNotasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        // Get all the incidenteDeProveedorList where notas does not contain
        defaultIncidenteDeProveedorFiltering("notas.doesNotContain=" + UPDATED_NOTAS, "notas.doesNotContain=" + DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByIncidenteIsEqualToSomething() throws Exception {
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);
            incidente = IncidenteResourceIT.createEntity();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        em.persist(incidente);
        em.flush();
        incidenteDeProveedor.setIncidente(incidente);
        incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);
        Long incidenteId = incidente.getId();
        // Get all the incidenteDeProveedorList where incidente equals to incidenteId
        defaultIncidenteDeProveedorShouldBeFound("incidenteId.equals=" + incidenteId);

        // Get all the incidenteDeProveedorList where incidente equals to (incidenteId + 1)
        defaultIncidenteDeProveedorShouldNotBeFound("incidenteId.equals=" + (incidenteId + 1));
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByServicioDeProveedorIsEqualToSomething() throws Exception {
        ServicioDeProveedor servicioDeProveedor;
        if (TestUtil.findAll(em, ServicioDeProveedor.class).isEmpty()) {
            incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);
            servicioDeProveedor = ServicioDeProveedorResourceIT.createEntity(em);
        } else {
            servicioDeProveedor = TestUtil.findAll(em, ServicioDeProveedor.class).get(0);
        }
        em.persist(servicioDeProveedor);
        em.flush();
        incidenteDeProveedor.setServicioDeProveedor(servicioDeProveedor);
        incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);
        Long servicioDeProveedorId = servicioDeProveedor.getId();
        // Get all the incidenteDeProveedorList where servicioDeProveedor equals to servicioDeProveedorId
        defaultIncidenteDeProveedorShouldBeFound("servicioDeProveedorId.equals=" + servicioDeProveedorId);

        // Get all the incidenteDeProveedorList where servicioDeProveedor equals to (servicioDeProveedorId + 1)
        defaultIncidenteDeProveedorShouldNotBeFound("servicioDeProveedorId.equals=" + (servicioDeProveedorId + 1));
    }

    @Test
    @Transactional
    void getAllIncidenteDeProveedorsByAbiertoPorIsEqualToSomething() throws Exception {
        User abiertoPor;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);
            abiertoPor = UserResourceIT.createEntity();
        } else {
            abiertoPor = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(abiertoPor);
        em.flush();
        incidenteDeProveedor.setAbiertoPor(abiertoPor);
        incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);
        Long abiertoPorId = abiertoPor.getId();
        // Get all the incidenteDeProveedorList where abiertoPor equals to abiertoPorId
        defaultIncidenteDeProveedorShouldBeFound("abiertoPorId.equals=" + abiertoPorId);

        // Get all the incidenteDeProveedorList where abiertoPor equals to (abiertoPorId + 1)
        defaultIncidenteDeProveedorShouldNotBeFound("abiertoPorId.equals=" + (abiertoPorId + 1));
    }

    private void defaultIncidenteDeProveedorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIncidenteDeProveedorShouldBeFound(shouldBeFound);
        defaultIncidenteDeProveedorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIncidenteDeProveedorShouldBeFound(String filter) throws Exception {
        restIncidenteDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incidenteDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticketExterno").value(hasItem(DEFAULT_TICKET_EXTERNO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].abiertoEn").value(hasItem(DEFAULT_ABIERTO_EN.toString())))
            .andExpect(jsonPath("$.[*].primeraRespuestaEn").value(hasItem(DEFAULT_PRIMERA_RESPUESTA_EN.toString())))
            .andExpect(jsonPath("$.[*].resueltoEn").value(hasItem(DEFAULT_RESUELTO_EN.toString())))
            .andExpect(jsonPath("$.[*].cumplioSla").value(hasItem(DEFAULT_CUMPLIO_SLA)))
            .andExpect(jsonPath("$.[*].motivoRechazo").value(hasItem(DEFAULT_MOTIVO_RECHAZO)))
            .andExpect(jsonPath("$.[*].responsableResuelto").value(hasItem(DEFAULT_RESPONSABLE_RESUELTO)))
            .andExpect(jsonPath("$.[*].nivelResuelto").value(hasItem(DEFAULT_NIVEL_RESUELTO.toString())))
            .andExpect(jsonPath("$.[*].huboCobertura").value(hasItem(DEFAULT_HUBO_COBERTURA)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));

        // Check, that the count call also returns 1
        restIncidenteDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIncidenteDeProveedorShouldNotBeFound(String filter) throws Exception {
        restIncidenteDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIncidenteDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIncidenteDeProveedor() throws Exception {
        // Get the incidenteDeProveedor
        restIncidenteDeProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncidenteDeProveedor() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incidenteDeProveedor
        IncidenteDeProveedor updatedIncidenteDeProveedor = incidenteDeProveedorRepository
            .findById(incidenteDeProveedor.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIncidenteDeProveedor are not directly saved in db
        em.detach(updatedIncidenteDeProveedor);
        updatedIncidenteDeProveedor
            .ticketExterno(UPDATED_TICKET_EXTERNO)
            .estado(UPDATED_ESTADO)
            .abiertoEn(UPDATED_ABIERTO_EN)
            .primeraRespuestaEn(UPDATED_PRIMERA_RESPUESTA_EN)
            .resueltoEn(UPDATED_RESUELTO_EN)
            .cumplioSla(UPDATED_CUMPLIO_SLA)
            .motivoRechazo(UPDATED_MOTIVO_RECHAZO)
            .responsableResuelto(UPDATED_RESPONSABLE_RESUELTO)
            .nivelResuelto(UPDATED_NIVEL_RESUELTO)
            .huboCobertura(UPDATED_HUBO_COBERTURA)
            .notas(UPDATED_NOTAS);
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(updatedIncidenteDeProveedor);

        restIncidenteDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incidenteDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incidenteDeProveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIncidenteDeProveedorToMatchAllProperties(updatedIncidenteDeProveedor);
    }

    @Test
    @Transactional
    void putNonExistingIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidenteDeProveedor.setId(longCount.incrementAndGet());

        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncidenteDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incidenteDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incidenteDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidenteDeProveedor.setId(longCount.incrementAndGet());

        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidenteDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(incidenteDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidenteDeProveedor.setId(longCount.incrementAndGet());

        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidenteDeProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(incidenteDeProveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncidenteDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incidenteDeProveedor using partial update
        IncidenteDeProveedor partialUpdatedIncidenteDeProveedor = new IncidenteDeProveedor();
        partialUpdatedIncidenteDeProveedor.setId(incidenteDeProveedor.getId());

        partialUpdatedIncidenteDeProveedor
            .resueltoEn(UPDATED_RESUELTO_EN)
            .responsableResuelto(UPDATED_RESPONSABLE_RESUELTO)
            .huboCobertura(UPDATED_HUBO_COBERTURA);

        restIncidenteDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncidenteDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncidenteDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the IncidenteDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncidenteDeProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIncidenteDeProveedor, incidenteDeProveedor),
            getPersistedIncidenteDeProveedor(incidenteDeProveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateIncidenteDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the incidenteDeProveedor using partial update
        IncidenteDeProveedor partialUpdatedIncidenteDeProveedor = new IncidenteDeProveedor();
        partialUpdatedIncidenteDeProveedor.setId(incidenteDeProveedor.getId());

        partialUpdatedIncidenteDeProveedor
            .ticketExterno(UPDATED_TICKET_EXTERNO)
            .estado(UPDATED_ESTADO)
            .abiertoEn(UPDATED_ABIERTO_EN)
            .primeraRespuestaEn(UPDATED_PRIMERA_RESPUESTA_EN)
            .resueltoEn(UPDATED_RESUELTO_EN)
            .cumplioSla(UPDATED_CUMPLIO_SLA)
            .motivoRechazo(UPDATED_MOTIVO_RECHAZO)
            .responsableResuelto(UPDATED_RESPONSABLE_RESUELTO)
            .nivelResuelto(UPDATED_NIVEL_RESUELTO)
            .huboCobertura(UPDATED_HUBO_COBERTURA)
            .notas(UPDATED_NOTAS);

        restIncidenteDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncidenteDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncidenteDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the IncidenteDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncidenteDeProveedorUpdatableFieldsEquals(
            partialUpdatedIncidenteDeProveedor,
            getPersistedIncidenteDeProveedor(partialUpdatedIncidenteDeProveedor)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidenteDeProveedor.setId(longCount.incrementAndGet());

        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncidenteDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incidenteDeProveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incidenteDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidenteDeProveedor.setId(longCount.incrementAndGet());

        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidenteDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(incidenteDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncidenteDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        incidenteDeProveedor.setId(longCount.incrementAndGet());

        // Create the IncidenteDeProveedor
        IncidenteDeProveedorDTO incidenteDeProveedorDTO = incidenteDeProveedorMapper.toDto(incidenteDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncidenteDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(incidenteDeProveedorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncidenteDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncidenteDeProveedor() throws Exception {
        // Initialize the database
        insertedIncidenteDeProveedor = incidenteDeProveedorRepository.saveAndFlush(incidenteDeProveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the incidenteDeProveedor
        restIncidenteDeProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, incidenteDeProveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return incidenteDeProveedorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected IncidenteDeProveedor getPersistedIncidenteDeProveedor(IncidenteDeProveedor incidenteDeProveedor) {
        return incidenteDeProveedorRepository.findById(incidenteDeProveedor.getId()).orElseThrow();
    }

    protected void assertPersistedIncidenteDeProveedorToMatchAllProperties(IncidenteDeProveedor expectedIncidenteDeProveedor) {
        assertIncidenteDeProveedorAllPropertiesEquals(
            expectedIncidenteDeProveedor,
            getPersistedIncidenteDeProveedor(expectedIncidenteDeProveedor)
        );
    }

    protected void assertPersistedIncidenteDeProveedorToMatchUpdatableProperties(IncidenteDeProveedor expectedIncidenteDeProveedor) {
        assertIncidenteDeProveedorAllUpdatablePropertiesEquals(
            expectedIncidenteDeProveedor,
            getPersistedIncidenteDeProveedor(expectedIncidenteDeProveedor)
        );
    }
}
