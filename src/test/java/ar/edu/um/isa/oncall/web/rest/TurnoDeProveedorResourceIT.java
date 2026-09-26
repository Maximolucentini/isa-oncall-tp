package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.TurnoDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import ar.edu.um.isa.oncall.repository.TurnoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.TurnoDeProveedorService;
import ar.edu.um.isa.oncall.service.dto.TurnoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.TurnoDeProveedorMapper;
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
 * Integration tests for the {@link TurnoDeProveedorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TurnoDeProveedorResourceIT {

    private static final Instant DEFAULT_DESDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DESDE = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_HASTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HASTA = Instant.ofEpochMilli(1701729143509L);

    private static final NivelGuardia DEFAULT_NIVEL = NivelGuardia.PRIMARIO;
    private static final NivelGuardia UPDATED_NIVEL = NivelGuardia.SECUNDARIO;

    private static final Boolean DEFAULT_ES_REEMPLAZO = false;
    private static final Boolean UPDATED_ES_REEMPLAZO = true;

    private static final String DEFAULT_NOTA = "AAAAAAAAAA";
    private static final String UPDATED_NOTA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/turno-de-proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TurnoDeProveedorRepository turnoDeProveedorRepository;

    @Mock
    private TurnoDeProveedorRepository turnoDeProveedorRepositoryMock;

    @Autowired
    private TurnoDeProveedorMapper turnoDeProveedorMapper;

    @Mock
    private TurnoDeProveedorService turnoDeProveedorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurnoDeProveedorMockMvc;

    private TurnoDeProveedor turnoDeProveedor;

    private TurnoDeProveedor insertedTurnoDeProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurnoDeProveedor createEntity(EntityManager em) {
        TurnoDeProveedor turnoDeProveedor = new TurnoDeProveedor()
            .desde(DEFAULT_DESDE)
            .hasta(DEFAULT_HASTA)
            .nivel(DEFAULT_NIVEL)
            .esReemplazo(DEFAULT_ES_REEMPLAZO)
            .nota(DEFAULT_NOTA);
        // Add required entity
        ContactoDeProveedor contactoDeProveedor;
        if (TestUtil.findAll(em, ContactoDeProveedor.class).isEmpty()) {
            contactoDeProveedor = ContactoDeProveedorResourceIT.createEntity(em);
            em.persist(contactoDeProveedor);
            em.flush();
        } else {
            contactoDeProveedor = TestUtil.findAll(em, ContactoDeProveedor.class).get(0);
        }
        turnoDeProveedor.setContacto(contactoDeProveedor);
        return turnoDeProveedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurnoDeProveedor createUpdatedEntity(EntityManager em) {
        TurnoDeProveedor updatedTurnoDeProveedor = new TurnoDeProveedor()
            .desde(UPDATED_DESDE)
            .hasta(UPDATED_HASTA)
            .nivel(UPDATED_NIVEL)
            .esReemplazo(UPDATED_ES_REEMPLAZO)
            .nota(UPDATED_NOTA);
        // Add required entity
        ContactoDeProveedor contactoDeProveedor;
        if (TestUtil.findAll(em, ContactoDeProveedor.class).isEmpty()) {
            contactoDeProveedor = ContactoDeProveedorResourceIT.createUpdatedEntity(em);
            em.persist(contactoDeProveedor);
            em.flush();
        } else {
            contactoDeProveedor = TestUtil.findAll(em, ContactoDeProveedor.class).get(0);
        }
        updatedTurnoDeProveedor.setContacto(contactoDeProveedor);
        return updatedTurnoDeProveedor;
    }

    @BeforeEach
    void initTest() {
        turnoDeProveedor = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTurnoDeProveedor != null) {
            turnoDeProveedorRepository.delete(insertedTurnoDeProveedor);
            insertedTurnoDeProveedor = null;
        }
    }

    @Test
    @Transactional
    void createTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);
        var returnedTurnoDeProveedorDTO = om.readValue(
            restTurnoDeProveedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TurnoDeProveedorDTO.class
        );

        // Validate the TurnoDeProveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTurnoDeProveedor = turnoDeProveedorMapper.toEntity(returnedTurnoDeProveedorDTO);
        assertTurnoDeProveedorUpdatableFieldsEquals(returnedTurnoDeProveedor, getPersistedTurnoDeProveedor(returnedTurnoDeProveedor));

        insertedTurnoDeProveedor = returnedTurnoDeProveedor;
    }

    @Test
    @Transactional
    void createTurnoDeProveedorWithExistingId() throws Exception {
        // Create the TurnoDeProveedor with an existing ID
        turnoDeProveedor.setId(1L);
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurnoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesdeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoDeProveedor.setDesde(null);

        // Create the TurnoDeProveedor, which fails.
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        restTurnoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHastaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoDeProveedor.setHasta(null);

        // Create the TurnoDeProveedor, which fails.
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        restTurnoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNivelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoDeProveedor.setNivel(null);

        // Create the TurnoDeProveedor, which fails.
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        restTurnoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEsReemplazoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turnoDeProveedor.setEsReemplazo(null);

        // Create the TurnoDeProveedor, which fails.
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        restTurnoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedors() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList
        restTurnoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnoDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].desde").value(hasItem(DEFAULT_DESDE.toString())))
            .andExpect(jsonPath("$.[*].hasta").value(hasItem(DEFAULT_HASTA.toString())))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())))
            .andExpect(jsonPath("$.[*].esReemplazo").value(hasItem(DEFAULT_ES_REEMPLAZO)))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTurnoDeProveedorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(turnoDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTurnoDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(turnoDeProveedorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTurnoDeProveedorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(turnoDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTurnoDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(turnoDeProveedorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTurnoDeProveedor() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get the turnoDeProveedor
        restTurnoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, turnoDeProveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turnoDeProveedor.getId().intValue()))
            .andExpect(jsonPath("$.desde").value(DEFAULT_DESDE.toString()))
            .andExpect(jsonPath("$.hasta").value(DEFAULT_HASTA.toString()))
            .andExpect(jsonPath("$.nivel").value(DEFAULT_NIVEL.toString()))
            .andExpect(jsonPath("$.esReemplazo").value(DEFAULT_ES_REEMPLAZO))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA));
    }

    @Test
    @Transactional
    void getTurnoDeProveedorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        Long id = turnoDeProveedor.getId();

        defaultTurnoDeProveedorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTurnoDeProveedorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTurnoDeProveedorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByDesdeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where desde equals to
        defaultTurnoDeProveedorFiltering("desde.equals=" + DEFAULT_DESDE, "desde.equals=" + UPDATED_DESDE);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByDesdeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where desde in
        defaultTurnoDeProveedorFiltering("desde.in=" + DEFAULT_DESDE + "," + UPDATED_DESDE, "desde.in=" + UPDATED_DESDE);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByDesdeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where desde is not null
        defaultTurnoDeProveedorFiltering("desde.specified=true", "desde.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByHastaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where hasta equals to
        defaultTurnoDeProveedorFiltering("hasta.equals=" + DEFAULT_HASTA, "hasta.equals=" + UPDATED_HASTA);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByHastaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where hasta in
        defaultTurnoDeProveedorFiltering("hasta.in=" + DEFAULT_HASTA + "," + UPDATED_HASTA, "hasta.in=" + UPDATED_HASTA);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByHastaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where hasta is not null
        defaultTurnoDeProveedorFiltering("hasta.specified=true", "hasta.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNivelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nivel equals to
        defaultTurnoDeProveedorFiltering("nivel.equals=" + DEFAULT_NIVEL, "nivel.equals=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNivelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nivel in
        defaultTurnoDeProveedorFiltering("nivel.in=" + DEFAULT_NIVEL + "," + UPDATED_NIVEL, "nivel.in=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNivelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nivel is not null
        defaultTurnoDeProveedorFiltering("nivel.specified=true", "nivel.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByEsReemplazoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where esReemplazo equals to
        defaultTurnoDeProveedorFiltering("esReemplazo.equals=" + DEFAULT_ES_REEMPLAZO, "esReemplazo.equals=" + UPDATED_ES_REEMPLAZO);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByEsReemplazoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where esReemplazo in
        defaultTurnoDeProveedorFiltering(
            "esReemplazo.in=" + DEFAULT_ES_REEMPLAZO + "," + UPDATED_ES_REEMPLAZO,
            "esReemplazo.in=" + UPDATED_ES_REEMPLAZO
        );
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByEsReemplazoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where esReemplazo is not null
        defaultTurnoDeProveedorFiltering("esReemplazo.specified=true", "esReemplazo.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNotaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nota equals to
        defaultTurnoDeProveedorFiltering("nota.equals=" + DEFAULT_NOTA, "nota.equals=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNotaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nota in
        defaultTurnoDeProveedorFiltering("nota.in=" + DEFAULT_NOTA + "," + UPDATED_NOTA, "nota.in=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNotaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nota is not null
        defaultTurnoDeProveedorFiltering("nota.specified=true", "nota.specified=false");
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNotaContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nota contains
        defaultTurnoDeProveedorFiltering("nota.contains=" + DEFAULT_NOTA, "nota.contains=" + UPDATED_NOTA);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByNotaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        // Get all the turnoDeProveedorList where nota does not contain
        defaultTurnoDeProveedorFiltering("nota.doesNotContain=" + UPDATED_NOTA, "nota.doesNotContain=" + DEFAULT_NOTA);
    }

    @Test
    @Transactional
    void getAllTurnoDeProveedorsByContactoIsEqualToSomething() throws Exception {
        ContactoDeProveedor contacto;
        if (TestUtil.findAll(em, ContactoDeProveedor.class).isEmpty()) {
            turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);
            contacto = ContactoDeProveedorResourceIT.createEntity(em);
        } else {
            contacto = TestUtil.findAll(em, ContactoDeProveedor.class).get(0);
        }
        em.persist(contacto);
        em.flush();
        turnoDeProveedor.setContacto(contacto);
        turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);
        Long contactoId = contacto.getId();
        // Get all the turnoDeProveedorList where contacto equals to contactoId
        defaultTurnoDeProveedorShouldBeFound("contactoId.equals=" + contactoId);

        // Get all the turnoDeProveedorList where contacto equals to (contactoId + 1)
        defaultTurnoDeProveedorShouldNotBeFound("contactoId.equals=" + (contactoId + 1));
    }

    private void defaultTurnoDeProveedorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTurnoDeProveedorShouldBeFound(shouldBeFound);
        defaultTurnoDeProveedorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTurnoDeProveedorShouldBeFound(String filter) throws Exception {
        restTurnoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turnoDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].desde").value(hasItem(DEFAULT_DESDE.toString())))
            .andExpect(jsonPath("$.[*].hasta").value(hasItem(DEFAULT_HASTA.toString())))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())))
            .andExpect(jsonPath("$.[*].esReemplazo").value(hasItem(DEFAULT_ES_REEMPLAZO)))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)));

        // Check, that the count call also returns 1
        restTurnoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTurnoDeProveedorShouldNotBeFound(String filter) throws Exception {
        restTurnoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTurnoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTurnoDeProveedor() throws Exception {
        // Get the turnoDeProveedor
        restTurnoDeProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTurnoDeProveedor() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turnoDeProveedor
        TurnoDeProveedor updatedTurnoDeProveedor = turnoDeProveedorRepository.findById(turnoDeProveedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTurnoDeProveedor are not directly saved in db
        em.detach(updatedTurnoDeProveedor);
        updatedTurnoDeProveedor
            .desde(UPDATED_DESDE)
            .hasta(UPDATED_HASTA)
            .nivel(UPDATED_NIVEL)
            .esReemplazo(UPDATED_ES_REEMPLAZO)
            .nota(UPDATED_NOTA);
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(updatedTurnoDeProveedor);

        restTurnoDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnoDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoDeProveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTurnoDeProveedorToMatchAllProperties(updatedTurnoDeProveedor);
    }

    @Test
    @Transactional
    void putNonExistingTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoDeProveedor.setId(longCount.incrementAndGet());

        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnoDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turnoDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoDeProveedor.setId(longCount.incrementAndGet());

        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turnoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoDeProveedor.setId(longCount.incrementAndGet());

        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoDeProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurnoDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turnoDeProveedor using partial update
        TurnoDeProveedor partialUpdatedTurnoDeProveedor = new TurnoDeProveedor();
        partialUpdatedTurnoDeProveedor.setId(turnoDeProveedor.getId());

        partialUpdatedTurnoDeProveedor.desde(UPDATED_DESDE).hasta(UPDATED_HASTA).nota(UPDATED_NOTA);

        restTurnoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurnoDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurnoDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the TurnoDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurnoDeProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTurnoDeProveedor, turnoDeProveedor),
            getPersistedTurnoDeProveedor(turnoDeProveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateTurnoDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turnoDeProveedor using partial update
        TurnoDeProveedor partialUpdatedTurnoDeProveedor = new TurnoDeProveedor();
        partialUpdatedTurnoDeProveedor.setId(turnoDeProveedor.getId());

        partialUpdatedTurnoDeProveedor
            .desde(UPDATED_DESDE)
            .hasta(UPDATED_HASTA)
            .nivel(UPDATED_NIVEL)
            .esReemplazo(UPDATED_ES_REEMPLAZO)
            .nota(UPDATED_NOTA);

        restTurnoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurnoDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurnoDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the TurnoDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurnoDeProveedorUpdatableFieldsEquals(
            partialUpdatedTurnoDeProveedor,
            getPersistedTurnoDeProveedor(partialUpdatedTurnoDeProveedor)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoDeProveedor.setId(longCount.incrementAndGet());

        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurnoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turnoDeProveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turnoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoDeProveedor.setId(longCount.incrementAndGet());

        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turnoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurnoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turnoDeProveedor.setId(longCount.incrementAndGet());

        // Create the TurnoDeProveedor
        TurnoDeProveedorDTO turnoDeProveedorDTO = turnoDeProveedorMapper.toDto(turnoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurnoDeProveedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(turnoDeProveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TurnoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurnoDeProveedor() throws Exception {
        // Initialize the database
        insertedTurnoDeProveedor = turnoDeProveedorRepository.saveAndFlush(turnoDeProveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the turnoDeProveedor
        restTurnoDeProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, turnoDeProveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return turnoDeProveedorRepository.count();
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

    protected TurnoDeProveedor getPersistedTurnoDeProveedor(TurnoDeProveedor turnoDeProveedor) {
        return turnoDeProveedorRepository.findById(turnoDeProveedor.getId()).orElseThrow();
    }

    protected void assertPersistedTurnoDeProveedorToMatchAllProperties(TurnoDeProveedor expectedTurnoDeProveedor) {
        assertTurnoDeProveedorAllPropertiesEquals(expectedTurnoDeProveedor, getPersistedTurnoDeProveedor(expectedTurnoDeProveedor));
    }

    protected void assertPersistedTurnoDeProveedorToMatchUpdatableProperties(TurnoDeProveedor expectedTurnoDeProveedor) {
        assertTurnoDeProveedorAllUpdatablePropertiesEquals(
            expectedTurnoDeProveedor,
            getPersistedTurnoDeProveedor(expectedTurnoDeProveedor)
        );
    }
}
