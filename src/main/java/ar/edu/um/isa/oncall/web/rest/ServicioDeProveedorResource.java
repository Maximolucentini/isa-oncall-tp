package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.ServicioDeProveedorRepository;
import ar.edu.um.isa.oncall.service.ServicioDeProveedorQueryService;
import ar.edu.um.isa.oncall.service.ServicioDeProveedorService;
import ar.edu.um.isa.oncall.service.criteria.ServicioDeProveedorCriteria;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.ServicioDeProveedor}.
 */
@RestController
@RequestMapping("/api/servicio-de-proveedors")
public class ServicioDeProveedorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioDeProveedorResource.class);

    private static final String ENTITY_NAME = "servicioDeProveedor";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final ServicioDeProveedorService servicioDeProveedorService;

    private final ServicioDeProveedorRepository servicioDeProveedorRepository;

    private final ServicioDeProveedorQueryService servicioDeProveedorQueryService;

    public ServicioDeProveedorResource(
        ServicioDeProveedorService servicioDeProveedorService,
        ServicioDeProveedorRepository servicioDeProveedorRepository,
        ServicioDeProveedorQueryService servicioDeProveedorQueryService
    ) {
        this.servicioDeProveedorService = servicioDeProveedorService;
        this.servicioDeProveedorRepository = servicioDeProveedorRepository;
        this.servicioDeProveedorQueryService = servicioDeProveedorQueryService;
    }

    /**
     * {@code POST  /servicio-de-proveedors} : Create a new servicioDeProveedor.
     *
     * @param servicioDeProveedorDTO the servicioDeProveedorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicioDeProveedorDTO, or with status {@code 400 (Bad Request)} if the servicioDeProveedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServicioDeProveedorDTO> createServicioDeProveedor(
        @Valid @RequestBody ServicioDeProveedorDTO servicioDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ServicioDeProveedor : {}", servicioDeProveedorDTO);
        if (servicioDeProveedorDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicioDeProveedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        servicioDeProveedorDTO = servicioDeProveedorService.save(servicioDeProveedorDTO);
        return ResponseEntity.created(new URI("/api/servicio-de-proveedors/" + servicioDeProveedorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, servicioDeProveedorDTO.getId().toString()))
            .body(servicioDeProveedorDTO);
    }

    /**
     * {@code PUT  /servicio-de-proveedors/:id} : Updates an existing servicioDeProveedor.
     *
     * @param id the id of the servicioDeProveedorDTO to save.
     * @param servicioDeProveedorDTO the servicioDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the servicioDeProveedorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicioDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDeProveedorDTO> updateServicioDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicioDeProveedorDTO servicioDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServicioDeProveedor : {}, {}", id, servicioDeProveedorDTO);
        if (servicioDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        servicioDeProveedorDTO = servicioDeProveedorService.update(servicioDeProveedorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioDeProveedorDTO.getId().toString()))
            .body(servicioDeProveedorDTO);
    }

    /**
     * {@code PATCH  /servicio-de-proveedors/:id} : Partial updates given fields of an existing servicioDeProveedor, field will ignore if it is null
     *
     * @param id the id of the servicioDeProveedorDTO to save.
     * @param servicioDeProveedorDTO the servicioDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the servicioDeProveedorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicioDeProveedorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicioDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServicioDeProveedorDTO> partialUpdateServicioDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicioDeProveedorDTO servicioDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServicioDeProveedor partially : {}, {}", id, servicioDeProveedorDTO);
        if (servicioDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicioDeProveedorDTO> result = servicioDeProveedorService.partialUpdate(servicioDeProveedorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, servicioDeProveedorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /servicio-de-proveedors} : get all the Servicio De Proveedors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Servicio De Proveedors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServicioDeProveedorDTO>> getAllServicioDeProveedors(
        ServicioDeProveedorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ServicioDeProveedors by criteria: {}", criteria);

        Page<ServicioDeProveedorDTO> page = servicioDeProveedorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /servicio-de-proveedors/count} : count all the servicioDeProveedors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countServicioDeProveedors(ServicioDeProveedorCriteria criteria) {
        LOG.debug("REST request to count ServicioDeProveedors by criteria: {}", criteria);
        return ResponseEntity.ok().body(servicioDeProveedorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /servicio-de-proveedors/:id} : get the "id" servicioDeProveedor.
     *
     * @param id the id of the servicioDeProveedorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicioDeProveedorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDeProveedorDTO> getServicioDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServicioDeProveedor : {}", id);
        Optional<ServicioDeProveedorDTO> servicioDeProveedorDTO = servicioDeProveedorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicioDeProveedorDTO);
    }

    /**
     * {@code DELETE  /servicio-de-proveedors/:id} : delete the "id" servicioDeProveedor.
     *
     * @param id the id of the servicioDeProveedorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicioDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServicioDeProveedor : {}", id);
        servicioDeProveedorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
