package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.ContactoDeProveedorAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.repository.ContactoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.ContactoDeProveedorService;
import ar.edu.um.isa.oncall.service.dto.ContactoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.ContactoDeProveedorMapper;
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
 * Integration tests for the {@link ContactoDeProveedorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactoDeProveedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_ROL = "AAAAAAAAAA";
    private static final String UPDATED_ROL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/contacto-de-proveedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContactoDeProveedorRepository contactoDeProveedorRepository;

    @Mock
    private ContactoDeProveedorRepository contactoDeProveedorRepositoryMock;

    @Autowired
    private ContactoDeProveedorMapper contactoDeProveedorMapper;

    @Mock
    private ContactoDeProveedorService contactoDeProveedorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactoDeProveedorMockMvc;

    private ContactoDeProveedor contactoDeProveedor;

    private ContactoDeProveedor insertedContactoDeProveedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactoDeProveedor createEntity(EntityManager em) {
        ContactoDeProveedor contactoDeProveedor = new ContactoDeProveedor()
            .nombre(DEFAULT_NOMBRE)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO)
            .rol(DEFAULT_ROL)
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
        contactoDeProveedor.setProveedor(proveedor);
        return contactoDeProveedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactoDeProveedor createUpdatedEntity(EntityManager em) {
        ContactoDeProveedor updatedContactoDeProveedor = new ContactoDeProveedor()
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .rol(UPDATED_ROL)
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
        updatedContactoDeProveedor.setProveedor(proveedor);
        return updatedContactoDeProveedor;
    }

    @BeforeEach
    void initTest() {
        contactoDeProveedor = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedContactoDeProveedor != null) {
            contactoDeProveedorRepository.delete(insertedContactoDeProveedor);
            insertedContactoDeProveedor = null;
        }
    }

    @Test
    @Transactional
    void createContactoDeProveedor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);
        var returnedContactoDeProveedorDTO = om.readValue(
            restContactoDeProveedorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoDeProveedorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContactoDeProveedorDTO.class
        );

        // Validate the ContactoDeProveedor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContactoDeProveedor = contactoDeProveedorMapper.toEntity(returnedContactoDeProveedorDTO);
        assertContactoDeProveedorUpdatableFieldsEquals(
            returnedContactoDeProveedor,
            getPersistedContactoDeProveedor(returnedContactoDeProveedor)
        );

        insertedContactoDeProveedor = returnedContactoDeProveedor;
    }

    @Test
    @Transactional
    void createContactoDeProveedorWithExistingId() throws Exception {
        // Create the ContactoDeProveedor with an existing ID
        contactoDeProveedor.setId(1L);
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoDeProveedor.setNombre(null);

        // Create the ContactoDeProveedor, which fails.
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        restContactoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoDeProveedor.setEmail(null);

        // Create the ContactoDeProveedor, which fails.
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        restContactoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contactoDeProveedor.setActivo(null);

        // Create the ContactoDeProveedor, which fails.
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        restContactoDeProveedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoDeProveedorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactoDeProveedors() throws Exception {
        // Initialize the database
        insertedContactoDeProveedor = contactoDeProveedorRepository.saveAndFlush(contactoDeProveedor);

        // Get all the contactoDeProveedorList
        restContactoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactoDeProveedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].rol").value(hasItem(DEFAULT_ROL)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactoDeProveedorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contactoDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactoDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contactoDeProveedorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactoDeProveedorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contactoDeProveedorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactoDeProveedorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contactoDeProveedorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContactoDeProveedor() throws Exception {
        // Initialize the database
        insertedContactoDeProveedor = contactoDeProveedorRepository.saveAndFlush(contactoDeProveedor);

        // Get the contactoDeProveedor
        restContactoDeProveedorMockMvc
            .perform(get(ENTITY_API_URL_ID, contactoDeProveedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactoDeProveedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.rol").value(DEFAULT_ROL))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingContactoDeProveedor() throws Exception {
        // Get the contactoDeProveedor
        restContactoDeProveedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactoDeProveedor() throws Exception {
        // Initialize the database
        insertedContactoDeProveedor = contactoDeProveedorRepository.saveAndFlush(contactoDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactoDeProveedor
        ContactoDeProveedor updatedContactoDeProveedor = contactoDeProveedorRepository.findById(contactoDeProveedor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactoDeProveedor are not directly saved in db
        em.detach(updatedContactoDeProveedor);
        updatedContactoDeProveedor
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .rol(UPDATED_ROL)
            .activo(UPDATED_ACTIVO);
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(updatedContactoDeProveedor);

        restContactoDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactoDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactoDeProveedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContactoDeProveedorToMatchAllProperties(updatedContactoDeProveedor);
    }

    @Test
    @Transactional
    void putNonExistingContactoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoDeProveedor.setId(longCount.incrementAndGet());

        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactoDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactoDeProveedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoDeProveedor.setId(longCount.incrementAndGet());

        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoDeProveedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contactoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoDeProveedor.setId(longCount.incrementAndGet());

        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoDeProveedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contactoDeProveedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactoDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedContactoDeProveedor = contactoDeProveedorRepository.saveAndFlush(contactoDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactoDeProveedor using partial update
        ContactoDeProveedor partialUpdatedContactoDeProveedor = new ContactoDeProveedor();
        partialUpdatedContactoDeProveedor.setId(contactoDeProveedor.getId());

        partialUpdatedContactoDeProveedor.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL).activo(UPDATED_ACTIVO);

        restContactoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactoDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactoDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the ContactoDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactoDeProveedorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContactoDeProveedor, contactoDeProveedor),
            getPersistedContactoDeProveedor(contactoDeProveedor)
        );
    }

    @Test
    @Transactional
    void fullUpdateContactoDeProveedorWithPatch() throws Exception {
        // Initialize the database
        insertedContactoDeProveedor = contactoDeProveedorRepository.saveAndFlush(contactoDeProveedor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contactoDeProveedor using partial update
        ContactoDeProveedor partialUpdatedContactoDeProveedor = new ContactoDeProveedor();
        partialUpdatedContactoDeProveedor.setId(contactoDeProveedor.getId());

        partialUpdatedContactoDeProveedor
            .nombre(UPDATED_NOMBRE)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO)
            .rol(UPDATED_ROL)
            .activo(UPDATED_ACTIVO);

        restContactoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactoDeProveedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContactoDeProveedor))
            )
            .andExpect(status().isOk());

        // Validate the ContactoDeProveedor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContactoDeProveedorUpdatableFieldsEquals(
            partialUpdatedContactoDeProveedor,
            getPersistedContactoDeProveedor(partialUpdatedContactoDeProveedor)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContactoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoDeProveedor.setId(longCount.incrementAndGet());

        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactoDeProveedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoDeProveedor.setId(longCount.incrementAndGet());

        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contactoDeProveedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactoDeProveedor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contactoDeProveedor.setId(longCount.incrementAndGet());

        // Create the ContactoDeProveedor
        ContactoDeProveedorDTO contactoDeProveedorDTO = contactoDeProveedorMapper.toDto(contactoDeProveedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactoDeProveedorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contactoDeProveedorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactoDeProveedor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactoDeProveedor() throws Exception {
        // Initialize the database
        insertedContactoDeProveedor = contactoDeProveedorRepository.saveAndFlush(contactoDeProveedor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contactoDeProveedor
        restContactoDeProveedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactoDeProveedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contactoDeProveedorRepository.count();
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

    protected ContactoDeProveedor getPersistedContactoDeProveedor(ContactoDeProveedor contactoDeProveedor) {
        return contactoDeProveedorRepository.findById(contactoDeProveedor.getId()).orElseThrow();
    }

    protected void assertPersistedContactoDeProveedorToMatchAllProperties(ContactoDeProveedor expectedContactoDeProveedor) {
        assertContactoDeProveedorAllPropertiesEquals(
            expectedContactoDeProveedor,
            getPersistedContactoDeProveedor(expectedContactoDeProveedor)
        );
    }

    protected void assertPersistedContactoDeProveedorToMatchUpdatableProperties(ContactoDeProveedor expectedContactoDeProveedor) {
        assertContactoDeProveedorAllUpdatablePropertiesEquals(
            expectedContactoDeProveedor,
            getPersistedContactoDeProveedor(expectedContactoDeProveedor)
        );
    }
}
