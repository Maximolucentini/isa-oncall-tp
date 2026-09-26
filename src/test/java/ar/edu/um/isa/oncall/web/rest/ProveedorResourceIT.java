package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ProveedorAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.enumeration.CoberturaProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.TipoProveedor;
import ar.edu.um.isa.oncall.repository.ProveedorRepository;
import ar.edu.um.isa.oncall.service.dto.ProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.ProveedorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProveedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProveedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final TipoProveedor DEFAULT_TIPO = TipoProveedor.NUBE;
    private static final TipoProveedor UPDATED_TIPO = TipoProveedor.PAGOS;

    private static final String DEFAULT_ZONA_HORARIA = "AAAAAAAAAA";
    private static final String UPDATED_ZONA_HORARIA = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_SOPORTE = "AAAAAAAAAA";
    private static final String UPDATED_URL_SOPORTE = "BBBBBBBBBB";

    private static final String DEFAULT_URL_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_ESTADO = "BBBBBBBBBB";

    private static final CoberturaProveedor DEFAULT_COBERTURA = CoberturaProveedor.VEINTICUATRO_SIETE;
    private static final CoberturaProveedor UPDATED_COBERTURA = CoberturaProveedor.HORARIO_HABIL;

    private static final Integer DEFAULT_SLA_RESPUESTA_MINUTOS = 1;
    private static final Integer UPDATED_SLA_RESPUESTA_MINUTOS = 2;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProveedorMockMvc;

    private Proveedor proveedor;

    private Proveedor insertedProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createEntity() {
        return new Proveedor()
            .nombre(DEFAULT_NOMBRE)
            .tipo(DEFAULT_TIPO)
            .zonaHoraria(DEFAULT_ZONA_HORARIA)
            .telefonoContacto(DEFAULT_TELEFONO_CONTACTO)
            .emailContacto(DEFAULT_EMAIL_CONTACTO)
            .urlSoporte(DEFAULT_URL_SOPORTE)
            .urlEstado(DEFAULT_URL_ESTADO)
            .cobertura(DEFAULT_COBERTURA)
            .slaRespuestaMinutos(DEFAULT_SLA_RESPUESTA_MINUTOS)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proveedor createUpdatedEntity() {
        return new Proveedor()
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .emailContacto(UPDATED_EMAIL_CONTACTO)
            .urlSoporte(UPDATED_URL_SOPORTE)
            .urlEstado(UPDATED_URL_ESTADO)
            .cobertura(UPDATED_COBERTURA)
            .slaRespuestaMinutos(UPDATED_SLA_RESPUESTA_MINUTOS)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        proveedor = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProveedor != null) {
            proveedorRepository.delete(insertedProveedor);
            insertedProveedor = null;
        }
    }

    @Test
    @Transactional
    void createProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);
        var returnedProveedorDTO = om.readValue(
            restProveedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProveedorDTO.class
        );

        // Validate the Proveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProveedor = proveedorMapper.toEntity(returnedProveedorDTO);
        assertProveedorUpdatableFieldsEquals(returnedProveedor, getPersistedProveedor(returnedProveedor));

        insertedProveedor = returnedProveedor;
    }

    @Test
    @Transactional
    void createProveedorWithExistingId() throws Exception {
        // Create the Proveedor with an existing ID
        proveedor.setId(1L);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setNombre(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setTipo(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZonaHorariaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setZonaHoraria(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoberturaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setCobertura(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proveedor.setActivo(null);

        // Create the Proveedor, which fails.
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        restProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProveedors() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get all the proveedorList
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].zonaHoraria").value(hasItem(DEFAULT_ZONA_HORARIA)))
            .andExpect(jsonPath("$.[*].telefonoContacto").value(hasItem(DEFAULT_TELEFONO_CONTACTO)))
            .andExpect(jsonPath("$.[*].emailContacto").value(hasItem(DEFAULT_EMAIL_CONTACTO)))
            .andExpect(jsonPath("$.[*].urlSoporte").value(hasItem(DEFAULT_URL_SOPORTE)))
            .andExpect(jsonPath("$.[*].urlEstado").value(hasItem(DEFAULT_URL_ESTADO)))
            .andExpect(jsonPath("$.[*].cobertura").value(hasItem(DEFAULT_COBERTURA.toString())))
            .andExpect(jsonPath("$.[*].slaRespuestaMinutos").value(hasItem(DEFAULT_SLA_RESPUESTA_MINUTOS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        // Get the proveedor
        restProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, proveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proveedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.zonaHoraria").value(DEFAULT_ZONA_HORARIA))
            .andExpect(jsonPath("$.telefonoContacto").value(DEFAULT_TELEFONO_CONTACTO))
            .andExpect(jsonPath("$.emailContacto").value(DEFAULT_EMAIL_CONTACTO))
            .andExpect(jsonPath("$.urlSoporte").value(DEFAULT_URL_SOPORTE))
            .andExpect(jsonPath("$.urlEstado").value(DEFAULT_URL_ESTADO))
            .andExpect(jsonPath("$.cobertura").value(DEFAULT_COBERTURA.toString()))
            .andExpect(jsonPath("$.slaRespuestaMinutos").value(DEFAULT_SLA_RESPUESTA_MINUTOS))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingProveedor() throws Exception {
        // Get the proveedor
        restProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor
        Proveedor updatedProveedor = proveedorRepository.findById(proveedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProveedor are not directly saved in db
        em.detach(updatedProveedor);
        updatedProveedor
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .emailContacto(UPDATED_EMAIL_CONTACTO)
            .urlSoporte(UPDATED_URL_SOPORTE)
            .urlEstado(UPDATED_URL_ESTADO)
            .cobertura(UPDATED_COBERTURA)
            .slaRespuestaMinutos(UPDATED_SLA_RESPUESTA_MINUTOS)
            .activo(UPDATED_ACTIVO);
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(updatedProveedor);

        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProveedorToMatchAllProperties(updatedProveedor);
    }

    @Test
    @Transactional
    void putNonExistingProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor
            .tipo(UPDATED_TIPO)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .emailContacto(UPDATED_EMAIL_CONTACTO)
            .urlEstado(UPDATED_URL_ESTADO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProveedor, proveedor),
            getPersistedProveedor(proveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proveedor using partial update
        Proveedor partialUpdatedProveedor = new Proveedor();
        partialUpdatedProveedor.setId(proveedor.getId());

        partialUpdatedProveedor
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .zonaHoraria(UPDATED_ZONA_HORARIA)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .emailContacto(UPDATED_EMAIL_CONTACTO)
            .urlSoporte(UPDATED_URL_SOPORTE)
            .urlEstado(UPDATED_URL_ESTADO)
            .cobertura(UPDATED_COBERTURA)
            .slaRespuestaMinutos(UPDATED_SLA_RESPUESTA_MINUTOS)
            .activo(UPDATED_ACTIVO);

        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProveedor))
            )
            .andExpect(status().isOk());

        // Validate the Proveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProveedorUpdatableFieldsEquals(partialUpdatedProveedor, getPersistedProveedor(partialUpdatedProveedor));
    }

    @Test
    @Transactional
    void patchNonExistingProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proveedor.setId(longCount.incrementAndGet());

        // Create the Proveedor
        ProveedorDTO proveedorDTO = proveedorMapper.toDto(proveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProveedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProveedor() throws Exception {
        // Initialize the database
        insertedProveedor = proveedorRepository.saveAndFlush(proveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proveedor
        restProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, proveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proveedorRepository.count();
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

    protected Proveedor getPersistedProveedor(Proveedor proveedor) {
        return proveedorRepository.findById(proveedor.getId()).orElseThrow();
    }

    protected void assertPersistedProveedorToMatchAllProperties(Proveedor expectedProveedor) {
        assertProveedorAllPropertiesEquals(expectedProveedor, getPersistedProveedor(expectedProveedor));
    }

    protected void assertPersistedProveedorToMatchUpdatableProperties(Proveedor expectedProveedor) {
        assertProveedorAllUpdatablePropertiesEquals(expectedProveedor, getPersistedProveedor(expectedProveedor));
    }
}
