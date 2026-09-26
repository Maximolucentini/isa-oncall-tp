package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.ContactoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.ContactoDeProveedorService;
import ar.edu.um.isa.oncall.service.dto.ContactoDeProveedorDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.ContactoDeProveedor}.
 */
@RestController
@RequestMapping("/api/contacto-de-proveedors")
public class ContactoDeProveedorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContactoDeProveedorResource.class);

    private static final String ENTITY_NAME = "contactoDeProveedor";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final ContactoDeProveedorService contactoDeProveedorService;

    private final ContactoDeProveedorRepository contactoDeProveedorRepository;

    public ContactoDeProveedorResource(
        ContactoDeProveedorService contactoDeProveedorService,
        ContactoDeProveedorRepository contactoDeProveedorRepository
    ) {
        this.contactoDeProveedorService = contactoDeProveedorService;
        this.contactoDeProveedorRepository = contactoDeProveedorRepository;
    }

    /**
     * {@code POST  /contacto-de-proveedors} : Create a new contactoDeProveedor.
     *
     * @param contactoDeProveedorDTO the contactoDeProveedorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactoDeProveedorDTO, or with status {@code 400 (Bad Request)} if the contactoDeProveedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactoDeProveedorDTO> createContactoDeProveedor(
        @Valid @RequestBody ContactoDeProveedorDTO contactoDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ContactoDeProveedor : {}", contactoDeProveedorDTO);
        if (contactoDeProveedorDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactoDeProveedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactoDeProveedorDTO = contactoDeProveedorService.save(contactoDeProveedorDTO);
        return ResponseEntity.created(new URI("/api/contacto-de-proveedors/" + contactoDeProveedorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contactoDeProveedorDTO.getId().toString()))
            .body(contactoDeProveedorDTO);
    }

    /**
     * {@code PUT  /contacto-de-proveedors/:id} : Updates an existing contactoDeProveedor.
     *
     * @param id the id of the contactoDeProveedorDTO to save.
     * @param contactoDeProveedorDTO the contactoDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactoDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the contactoDeProveedorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactoDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactoDeProveedorDTO> updateContactoDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactoDeProveedorDTO contactoDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContactoDeProveedor : {}, {}", id, contactoDeProveedorDTO);
        if (contactoDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactoDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactoDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contactoDeProveedorDTO = contactoDeProveedorService.update(contactoDeProveedorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactoDeProveedorDTO.getId().toString()))
            .body(contactoDeProveedorDTO);
    }

    /**
     * {@code PATCH  /contacto-de-proveedors/:id} : Partial updates given fields of an existing contactoDeProveedor, field will ignore if it is null
     *
     * @param id the id of the contactoDeProveedorDTO to save.
     * @param contactoDeProveedorDTO the contactoDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactoDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the contactoDeProveedorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactoDeProveedorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactoDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactoDeProveedorDTO> partialUpdateContactoDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactoDeProveedorDTO contactoDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContactoDeProveedor partially : {}, {}", id, contactoDeProveedorDTO);
        if (contactoDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactoDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactoDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactoDeProveedorDTO> result = contactoDeProveedorService.partialUpdate(contactoDeProveedorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactoDeProveedorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contacto-de-proveedors} : get all the Contacto De Proveedors.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Contacto De Proveedors in body.
     */
    @GetMapping("")
    public List<ContactoDeProveedorDTO> getAllContactoDeProveedors(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ContactoDeProveedors");
        return contactoDeProveedorService.findAll();
    }

    /**
     * {@code GET  /contacto-de-proveedors/:id} : get the "id" contactoDeProveedor.
     *
     * @param id the id of the contactoDeProveedorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactoDeProveedorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactoDeProveedorDTO> getContactoDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContactoDeProveedor : {}", id);
        Optional<ContactoDeProveedorDTO> contactoDeProveedorDTO = contactoDeProveedorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactoDeProveedorDTO);
    }

    /**
     * {@code DELETE  /contacto-de-proveedors/:id} : delete the "id" contactoDeProveedor.
     *
     * @param id the id of the contactoDeProveedorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactoDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContactoDeProveedor : {}", id);
        contactoDeProveedorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
