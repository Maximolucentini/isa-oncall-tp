package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.IncidenteDeProveedorRepository;
import ar.edu.um.isa.oncall.service.IncidenteDeProveedorQueryService;
import ar.edu.um.isa.oncall.service.IncidenteDeProveedorService;
import ar.edu.um.isa.oncall.service.criteria.IncidenteDeProveedorCriteria;
import ar.edu.um.isa.oncall.service.dto.IncidenteDeProveedorDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.IncidenteDeProveedor}.
 */
@RestController
@RequestMapping("/api/incidente-de-proveedors")
public class IncidenteDeProveedorResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncidenteDeProveedorResource.class);

    private static final String ENTITY_NAME = "incidenteDeProveedor";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final IncidenteDeProveedorService incidenteDeProveedorService;

    private final IncidenteDeProveedorRepository incidenteDeProveedorRepository;

    private final IncidenteDeProveedorQueryService incidenteDeProveedorQueryService;

    public IncidenteDeProveedorResource(
        IncidenteDeProveedorService incidenteDeProveedorService,
        IncidenteDeProveedorRepository incidenteDeProveedorRepository,
        IncidenteDeProveedorQueryService incidenteDeProveedorQueryService
    ) {
        this.incidenteDeProveedorService = incidenteDeProveedorService;
        this.incidenteDeProveedorRepository = incidenteDeProveedorRepository;
        this.incidenteDeProveedorQueryService = incidenteDeProveedorQueryService;
    }

    /**
     * {@code POST  /incidente-de-proveedors} : Create a new incidenteDeProveedor.
     *
     * @param incidenteDeProveedorDTO the incidenteDeProveedorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incidenteDeProveedorDTO, or with status {@code 400 (Bad Request)} if the incidenteDeProveedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IncidenteDeProveedorDTO> createIncidenteDeProveedor(
        @Valid @RequestBody IncidenteDeProveedorDTO incidenteDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save IncidenteDeProveedor : {}", incidenteDeProveedorDTO);
        if (incidenteDeProveedorDTO.getId() != null) {
            throw new BadRequestAlertException("A new incidenteDeProveedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        incidenteDeProveedorDTO = incidenteDeProveedorService.save(incidenteDeProveedorDTO);
        return ResponseEntity.created(new URI("/api/incidente-de-proveedors/" + incidenteDeProveedorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, incidenteDeProveedorDTO.getId().toString()))
            .body(incidenteDeProveedorDTO);
    }

    /**
     * {@code PUT  /incidente-de-proveedors/:id} : Updates an existing incidenteDeProveedor.
     *
     * @param id the id of the incidenteDeProveedorDTO to save.
     * @param incidenteDeProveedorDTO the incidenteDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incidenteDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the incidenteDeProveedorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incidenteDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncidenteDeProveedorDTO> updateIncidenteDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncidenteDeProveedorDTO incidenteDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IncidenteDeProveedor : {}, {}", id, incidenteDeProveedorDTO);
        if (incidenteDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incidenteDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incidenteDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        incidenteDeProveedorDTO = incidenteDeProveedorService.update(incidenteDeProveedorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incidenteDeProveedorDTO.getId().toString()))
            .body(incidenteDeProveedorDTO);
    }

    /**
     * {@code PATCH  /incidente-de-proveedors/:id} : Partial updates given fields of an existing incidenteDeProveedor, field will ignore if it is null
     *
     * @param id the id of the incidenteDeProveedorDTO to save.
     * @param incidenteDeProveedorDTO the incidenteDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incidenteDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the incidenteDeProveedorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the incidenteDeProveedorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the incidenteDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncidenteDeProveedorDTO> partialUpdateIncidenteDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncidenteDeProveedorDTO incidenteDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IncidenteDeProveedor partially : {}, {}", id, incidenteDeProveedorDTO);
        if (incidenteDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incidenteDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incidenteDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncidenteDeProveedorDTO> result = incidenteDeProveedorService.partialUpdate(incidenteDeProveedorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incidenteDeProveedorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /incidente-de-proveedors} : get all the Incidente De Proveedors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Incidente De Proveedors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IncidenteDeProveedorDTO>> getAllIncidenteDeProveedors(
        IncidenteDeProveedorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IncidenteDeProveedors by criteria: {}", criteria);

        Page<IncidenteDeProveedorDTO> page = incidenteDeProveedorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /incidente-de-proveedors/count} : count all the incidenteDeProveedors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIncidenteDeProveedors(IncidenteDeProveedorCriteria criteria) {
        LOG.debug("REST request to count IncidenteDeProveedors by criteria: {}", criteria);
        return ResponseEntity.ok().body(incidenteDeProveedorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /incidente-de-proveedors/:id} : get the "id" incidenteDeProveedor.
     *
     * @param id the id of the incidenteDeProveedorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incidenteDeProveedorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncidenteDeProveedorDTO> getIncidenteDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IncidenteDeProveedor : {}", id);
        Optional<IncidenteDeProveedorDTO> incidenteDeProveedorDTO = incidenteDeProveedorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incidenteDeProveedorDTO);
    }

    /**
     * {@code DELETE  /incidente-de-proveedors/:id} : delete the "id" incidenteDeProveedor.
     *
     * @param id the id of the incidenteDeProveedorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidenteDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IncidenteDeProveedor : {}", id);
        incidenteDeProveedorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
