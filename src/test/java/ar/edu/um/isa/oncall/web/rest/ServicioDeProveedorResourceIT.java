package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ServicioDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.repository.ServicioDeProveedorRepository;
import ar.edu.um.isa.oncall.service.ServicioDeProveedorService;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioDeProveedorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ServicioDeProveedorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicioDeProveedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SLA_RESPUESTA_MINUTOS = 1;
    private static final Integer UPDATED_SLA_RESPUESTA_MINUTOS = 2;
    private static final Integer SMALLER_SLA_RESPUESTA_MINUTOS = 1 - 1;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/servicio-de-proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicioDeProveedorRepository servicioDeProveedorRepository;

    @Mock
    private ServicioDeProveedorRepository servicioDeProveedorRepositoryMock;

    @Autowired
    private ServicioDeProveedorMapper servicioDeProveedorMapper;

    @Mock
    private ServicioDeProveedorService servicioDeProveedorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioDeProveedorMockMvc;

    private ServicioDeProveedor servicioDeProveedor;

    private ServicioDeProveedor insertedServicioDeProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioDeProveedor createEntity(EntityManager em) {
        ServicioDeProveedor servicioDeProveedor = new ServicioDeProveedor()
            .nombre(DEFAULT_NOMBRE)
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .descripcion(DEFAULT_DESCRIPCION)
            .slaRespuestaMinutos(DEFAULT_SLA_RESPUESTA_MINUTOS)
            .activo(DEFAULT_ACTIVO);
        // Add required entity
        Proveedor proveedor;
        if (TestUtil.findAll(em, Proveedor.class).isEmpty()) {
            proveedor = ProveedorResourceIT.createEntity();
            em.persist(proveedor);
            em.flush();
        } else {
            proveedor = TestUtil.findAll(em, Proveedor.class).get(0);
        }
        servicioDeProveedor.setProveedor(proveedor);
        return servicioDeProveedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServicioDeProveedor createUpdatedEntity(EntityManager em) {
        ServicioDeProveedor updatedServicioDeProveedor = new ServicioDeProveedor()
            .nombre(UPDATED_NOMBRE)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .descripcion(UPDATED_DESCRIPCION)
            .slaRespuestaMinutos(UPDATED_SLA_RESPUESTA_MINUTOS)
            .activo(UPDATED_ACTIVO);
        // Add required entity
        Proveedor proveedor;
        if (TestUtil.findAll(em, Proveedor.class).isEmpty()) {
            proveedor = ProveedorResourceIT.createUpdatedEntity();
            em.persist(proveedor);
            em.flush();
        } else {
            proveedor = TestUtil.findAll(em, Proveedor.class).get(0);
        }
        updatedServicioDeProveedor.setProveedor(proveedor);
        return updatedServicioDeProveedor;
    }

    @BeforeEach
    void initTest() {
        servicioDeProveedor = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicioDeProveedor != null) {
            servicioDeProveedorRepository.delete(insertedServicioDeProveedor);
            insertedServicioDeProveedor = null;
        }
    }

    @Test
    @Transactional
    void createServicioDeProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);
        var returnedServicioDeProveedorDTO = om.readValue(
            restServicioDeProveedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDeProveedorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicioDeProveedorDTO.class
        );

        // Validate the ServicioDeProveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicioDeProveedor = servicioDeProveedorMapper.toEntity(returnedServicioDeProveedorDTO);
        assertServicioDeProveedorUpdatableFieldsEquals(
            returnedServicioDeProveedor,
            getPersistedServicioDeProveedor(returnedServicioDeProveedor)
        );

        insertedServicioDeProveedor = returnedServicioDeProveedor;
    }

    @Test
    @Transactional
    void createServicioDeProveedorWithExistingId() throws Exception {
        // Create the ServicioDeProveedor with an existing ID
        servicioDeProveedor.setId(1L);
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicioDeProveedor.setNombre(null);

        // Create the ServicioDeProveedor, which fails.
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        restServicioDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicioDeProveedor.setActivo(null);

        // Create the ServicioDeProveedor, which fails.
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        restServicioDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedors() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList
        restServicioDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicioDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].slaRespuestaMinutos").value(hasItem(DEFAULT_SLA_RESPUESTA_MINUTOS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioDeProveedorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicioDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioDeProveedorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicioDeProveedorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicioDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicioDeProveedorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServicioDeProveedor() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get the servicioDeProveedor
        restServicioDeProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, servicioDeProveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicioDeProveedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.slaRespuestaMinutos").value(DEFAULT_SLA_RESPUESTA_MINUTOS))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getServicioDeProveedorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        Long id = servicioDeProveedor.getId();

        defaultServicioDeProveedorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServicioDeProveedorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServicioDeProveedorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where nombre equals to
        defaultServicioDeProveedorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where nombre in
        defaultServicioDeProveedorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where nombre is not null
        defaultServicioDeProveedorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where nombre contains
        defaultServicioDeProveedorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where nombre does not contain
        defaultServicioDeProveedorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByIdentificadorExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where identificadorExterno equals to
        defaultServicioDeProveedorFiltering(
            "identificadorExterno.equals=" + DEFAULT_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.equals=" + UPDATED_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByIdentificadorExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where identificadorExterno in
        defaultServicioDeProveedorFiltering(
            "identificadorExterno.in=" + DEFAULT_IDENTIFICADOR_EXTERNO + "," + UPDATED_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.in=" + UPDATED_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByIdentificadorExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where identificadorExterno is not null
        defaultServicioDeProveedorFiltering("identificadorExterno.specified=true", "identificadorExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByIdentificadorExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where identificadorExterno contains
        defaultServicioDeProveedorFiltering(
            "identificadorExterno.contains=" + DEFAULT_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.contains=" + UPDATED_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByIdentificadorExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where identificadorExterno does not contain
        defaultServicioDeProveedorFiltering(
            "identificadorExterno.doesNotContain=" + UPDATED_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.doesNotContain=" + DEFAULT_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where descripcion equals to
        defaultServicioDeProveedorFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where descripcion in
        defaultServicioDeProveedorFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where descripcion is not null
        defaultServicioDeProveedorFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where descripcion contains
        defaultServicioDeProveedorFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where descripcion does not contain
        defaultServicioDeProveedorFiltering(
            "descripcion.doesNotContain=" + UPDATED_DESCRIPCION,
            "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos equals to
        defaultServicioDeProveedorFiltering(
            "slaRespuestaMinutos.equals=" + DEFAULT_SLA_RESPUESTA_MINUTOS,
            "slaRespuestaMinutos.equals=" + UPDATED_SLA_RESPUESTA_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos in
        defaultServicioDeProveedorFiltering(
            "slaRespuestaMinutos.in=" + DEFAULT_SLA_RESPUESTA_MINUTOS + "," + UPDATED_SLA_RESPUESTA_MINUTOS,
            "slaRespuestaMinutos.in=" + UPDATED_SLA_RESPUESTA_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos is not null
        defaultServicioDeProveedorFiltering("slaRespuestaMinutos.specified=true", "slaRespuestaMinutos.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos is greater than or equal to
        defaultServicioDeProveedorFiltering(
            "slaRespuestaMinutos.greaterThanOrEqual=" + DEFAULT_SLA_RESPUESTA_MINUTOS,
            "slaRespuestaMinutos.greaterThanOrEqual=" + (DEFAULT_SLA_RESPUESTA_MINUTOS + 1)
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos is less than or equal to
        defaultServicioDeProveedorFiltering(
            "slaRespuestaMinutos.lessThanOrEqual=" + DEFAULT_SLA_RESPUESTA_MINUTOS,
            "slaRespuestaMinutos.lessThanOrEqual=" + SMALLER_SLA_RESPUESTA_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos is less than
        defaultServicioDeProveedorFiltering(
            "slaRespuestaMinutos.lessThan=" + (DEFAULT_SLA_RESPUESTA_MINUTOS + 1),
            "slaRespuestaMinutos.lessThan=" + DEFAULT_SLA_RESPUESTA_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsBySlaRespuestaMinutosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where slaRespuestaMinutos is greater than
        defaultServicioDeProveedorFiltering(
            "slaRespuestaMinutos.greaterThan=" + SMALLER_SLA_RESPUESTA_MINUTOS,
            "slaRespuestaMinutos.greaterThan=" + DEFAULT_SLA_RESPUESTA_MINUTOS
        );
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where activo equals to
        defaultServicioDeProveedorFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where activo in
        defaultServicioDeProveedorFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        // Get all the servicioDeProveedorList where activo is not null
        defaultServicioDeProveedorFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByProveedorIsEqualToSomething() throws Exception {
        Proveedor proveedor;
        if (TestUtil.findAll(em, Proveedor.class).isEmpty()) {
            servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);
            proveedor = ProveedorResourceIT.createEntity();
        } else {
            proveedor = TestUtil.findAll(em, Proveedor.class).get(0);
        }
        em.persist(proveedor);
        em.flush();
        servicioDeProveedor.setProveedor(proveedor);
        servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);
        Long proveedorId = proveedor.getId();
        // Get all the servicioDeProveedorList where proveedor equals to proveedorId
        defaultServicioDeProveedorShouldBeFound("proveedorId.equals=" + proveedorId);

        // Get all the servicioDeProveedorList where proveedor equals to (proveedorId + 1)
        defaultServicioDeProveedorShouldNotBeFound("proveedorId.equals=" + (proveedorId + 1));
    }

    @Test
    @Transactional
    void getAllServicioDeProveedorsByServicioInternoIsEqualToSomething() throws Exception {
        Servicio servicioInterno;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);
            servicioInterno = ServicioResourceIT.createEntity(em);
        } else {
            servicioInterno = TestUtil.findAll(em, Servicio.class).get(0);
        }
        em.persist(servicioInterno);
        em.flush();
        servicioDeProveedor.addServicioInterno(servicioInterno);
        servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);
        Long servicioInternoId = servicioInterno.getId();
        // Get all the servicioDeProveedorList where servicioInterno equals to servicioInternoId
        defaultServicioDeProveedorShouldBeFound("servicioInternoId.equals=" + servicioInternoId);

        // Get all the servicioDeProveedorList where servicioInterno equals to (servicioInternoId + 1)
        defaultServicioDeProveedorShouldNotBeFound("servicioInternoId.equals=" + (servicioInternoId + 1));
    }

    private void defaultServicioDeProveedorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServicioDeProveedorShouldBeFound(shouldBeFound);
        defaultServicioDeProveedorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicioDeProveedorShouldBeFound(String filter) throws Exception {
        restServicioDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicioDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].slaRespuestaMinutos").value(hasItem(DEFAULT_SLA_RESPUESTA_MINUTOS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));

        // Check, that the count call also returns 1
        restServicioDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicioDeProveedorShouldNotBeFound(String filter) throws Exception {
        restServicioDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicioDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicioDeProveedor() throws Exception {
        // Get the servicioDeProveedor
        restServicioDeProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicioDeProveedor() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioDeProveedor
        ServicioDeProveedor updatedServicioDeProveedor = servicioDeProveedorRepository.findById(servicioDeProveedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicioDeProveedor are not directly saved in db
        em.detach(updatedServicioDeProveedor);
        updatedServicioDeProveedor
            .nombre(UPDATED_NOMBRE)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .descripcion(UPDATED_DESCRIPCION)
            .slaRespuestaMinutos(UPDATED_SLA_RESPUESTA_MINUTOS)
            .activo(UPDATED_ACTIVO);
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(updatedServicioDeProveedor);

        restServicioDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDeProveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicioDeProveedorToMatchAllProperties(updatedServicioDeProveedor);
    }

    @Test
    @Transactional
    void putNonExistingServicioDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioDeProveedor.setId(longCount.incrementAndGet());

        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicioDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioDeProveedor.setId(longCount.incrementAndGet());

        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicioDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioDeProveedor.setId(longCount.incrementAndGet());

        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioDeProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDeProveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioDeProveedor using partial update
        ServicioDeProveedor partialUpdatedServicioDeProveedor = new ServicioDeProveedor();
        partialUpdatedServicioDeProveedor.setId(servicioDeProveedor.getId());

        partialUpdatedServicioDeProveedor.nombre(UPDATED_NOMBRE).activo(UPDATED_ACTIVO);

        restServicioDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the ServicioDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioDeProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServicioDeProveedor, servicioDeProveedor),
            getPersistedServicioDeProveedor(servicioDeProveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateServicioDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicioDeProveedor using partial update
        ServicioDeProveedor partialUpdatedServicioDeProveedor = new ServicioDeProveedor();
        partialUpdatedServicioDeProveedor.setId(servicioDeProveedor.getId());

        partialUpdatedServicioDeProveedor
            .nombre(UPDATED_NOMBRE)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .descripcion(UPDATED_DESCRIPCION)
            .slaRespuestaMinutos(UPDATED_SLA_RESPUESTA_MINUTOS)
            .activo(UPDATED_ACTIVO);

        restServicioDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicioDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicioDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the ServicioDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioDeProveedorUpdatableFieldsEquals(
            partialUpdatedServicioDeProveedor,
            getPersistedServicioDeProveedor(partialUpdatedServicioDeProveedor)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServicioDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioDeProveedor.setId(longCount.incrementAndGet());

        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioDeProveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicioDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioDeProveedor.setId(longCount.incrementAndGet());

        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicioDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicioDeProveedor.setId(longCount.incrementAndGet());

        // Create the ServicioDeProveedor
        ServicioDeProveedorDTO servicioDeProveedorDTO = servicioDeProveedorMapper.toDto(servicioDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicioDeProveedorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServicioDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicioDeProveedor() throws Exception {
        // Initialize the database
        insertedServicioDeProveedor = servicioDeProveedorRepository.saveAndFlush(servicioDeProveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicioDeProveedor
        restServicioDeProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicioDeProveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicioDeProveedorRepository.count();
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

    protected ServicioDeProveedor getPersistedServicioDeProveedor(ServicioDeProveedor servicioDeProveedor) {
        return servicioDeProveedorRepository.findById(servicioDeProveedor.getId()).orElseThrow();
    }

    protected void assertPersistedServicioDeProveedorToMatchAllProperties(ServicioDeProveedor expectedServicioDeProveedor) {
        assertServicioDeProveedorAllPropertiesEquals(
            expectedServicioDeProveedor,
            getPersistedServicioDeProveedor(expectedServicioDeProveedor)
        );
    }

    protected void assertPersistedServicioDeProveedorToMatchUpdatableProperties(ServicioDeProveedor expectedServicioDeProveedor) {
        assertServicioDeProveedorAllUpdatablePropertiesEquals(
            expectedServicioDeProveedor,
            getPersistedServicioDeProveedor(expectedServicioDeProveedor)
        );
    }
}
