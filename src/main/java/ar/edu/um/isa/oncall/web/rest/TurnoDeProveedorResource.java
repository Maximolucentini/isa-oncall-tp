package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.TurnoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.TurnoDeProveedorQueryService;
import ar.edu.um.isa.oncall.service.TurnoDeProveedorService;
import ar.edu.um.isa.oncall.service.criteria.TurnoDeProveedorCriteria;
import ar.edu.um.isa.oncall.service.dto.TurnoDeProveedorDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.TurnoDeProveedor}.
 */
@RestController
@RequestMapping("/api/turno-de-proveedors")
public class TurnoDeProveedorResource {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoDeProveedorResource.class);

    private static final String ENTITY_NAME = "turnoDeProveedor";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final TurnoDeProveedorService turnoDeProveedorService;

    private final TurnoDeProveedorRepository turnoDeProveedorRepository;

    private final TurnoDeProveedorQueryService turnoDeProveedorQueryService;

    public TurnoDeProveedorResource(
        TurnoDeProveedorService turnoDeProveedorService,
        TurnoDeProveedorRepository turnoDeProveedorRepository,
        TurnoDeProveedorQueryService turnoDeProveedorQueryService
    ) {
        this.turnoDeProveedorService = turnoDeProveedorService;
        this.turnoDeProveedorRepository = turnoDeProveedorRepository;
        this.turnoDeProveedorQueryService = turnoDeProveedorQueryService;
    }

    /**
     * {@code POST  /turno-de-proveedors} : Create a new turnoDeProveedor.
     *
     * @param turnoDeProveedorDTO the turnoDeProveedorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turnoDeProveedorDTO, or with status {@code 400 (Bad Request)} if the turnoDeProveedor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TurnoDeProveedorDTO> createTurnoDeProveedor(@Valid @RequestBody TurnoDeProveedorDTO turnoDeProveedorDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TurnoDeProveedor : {}", turnoDeProveedorDTO);
        if (turnoDeProveedorDTO.getId() != null) {
            throw new BadRequestAlertException("A new turnoDeProveedor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        turnoDeProveedorDTO = turnoDeProveedorService.save(turnoDeProveedorDTO);
        return ResponseEntity.created(new URI("/api/turno-de-proveedors/" + turnoDeProveedorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, turnoDeProveedorDTO.getId().toString()))
            .body(turnoDeProveedorDTO);
    }

    /**
     * {@code PUT  /turno-de-proveedors/:id} : Updates an existing turnoDeProveedor.
     *
     * @param id the id of the turnoDeProveedorDTO to save.
     * @param turnoDeProveedorDTO the turnoDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnoDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the turnoDeProveedorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turnoDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TurnoDeProveedorDTO> updateTurnoDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TurnoDeProveedorDTO turnoDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TurnoDeProveedor : {}, {}", id, turnoDeProveedorDTO);
        if (turnoDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnoDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnoDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        turnoDeProveedorDTO = turnoDeProveedorService.update(turnoDeProveedorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turnoDeProveedorDTO.getId().toString()))
            .body(turnoDeProveedorDTO);
    }

    /**
     * {@code PATCH  /turno-de-proveedors/:id} : Partial updates given fields of an existing turnoDeProveedor, field will ignore if it is null
     *
     * @param id the id of the turnoDeProveedorDTO to save.
     * @param turnoDeProveedorDTO the turnoDeProveedorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turnoDeProveedorDTO,
     * or with status {@code 400 (Bad Request)} if the turnoDeProveedorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the turnoDeProveedorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the turnoDeProveedorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TurnoDeProveedorDTO> partialUpdateTurnoDeProveedor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TurnoDeProveedorDTO turnoDeProveedorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TurnoDeProveedor partially : {}, {}", id, turnoDeProveedorDTO);
        if (turnoDeProveedorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turnoDeProveedorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turnoDeProveedorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TurnoDeProveedorDTO> result = turnoDeProveedorService.partialUpdate(turnoDeProveedorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turnoDeProveedorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /turno-de-proveedors} : get all the Turno De Proveedors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Turno De Proveedors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TurnoDeProveedorDTO>> getAllTurnoDeProveedors(
        TurnoDeProveedorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TurnoDeProveedors by criteria: {}", criteria);

        Page<TurnoDeProveedorDTO> page = turnoDeProveedorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /turno-de-proveedors/count} : count all the turnoDeProveedors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTurnoDeProveedors(TurnoDeProveedorCriteria criteria) {
        LOG.debug("REST request to count TurnoDeProveedors by criteria: {}", criteria);
        return ResponseEntity.ok().body(turnoDeProveedorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /turno-de-proveedors/:id} : get the "id" turnoDeProveedor.
     *
     * @param id the id of the turnoDeProveedorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turnoDeProveedorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurnoDeProveedorDTO> getTurnoDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TurnoDeProveedor : {}", id);
        Optional<TurnoDeProveedorDTO> turnoDeProveedorDTO = turnoDeProveedorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turnoDeProveedorDTO);
    }

    /**
     * {@code DELETE  /turno-de-proveedors/:id} : delete the "id" turnoDeProveedor.
     *
     * @param id the id of the turnoDeProveedorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurnoDeProveedor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TurnoDeProveedor : {}", id);
        turnoDeProveedorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
