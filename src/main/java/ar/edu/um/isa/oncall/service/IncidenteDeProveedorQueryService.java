package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import ar.edu.um.isa.oncall.repository.IncidenteDeProveedorRepository;
import ar.edu.um.isa.oncall.service.criteria.IncidenteDeProveedorCriteria;
import ar.edu.um.isa.oncall.service.dto.IncidenteDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.IncidenteDeProveedorMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IncidenteDeProveedor} entities in the database.
 * The main input is a {@link IncidenteDeProveedorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IncidenteDeProveedorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IncidenteDeProveedorQueryService extends QueryService<IncidenteDeProveedor> {

    private static final Logger LOG = LoggerFactory.getLogger(IncidenteDeProveedorQueryService.class);

    private final IncidenteDeProveedorRepository incidenteDeProveedorRepository;

    private final IncidenteDeProveedorMapper incidenteDeProveedorMapper;

    public IncidenteDeProveedorQueryService(
        IncidenteDeProveedorRepository incidenteDeProveedorRepository,
        IncidenteDeProveedorMapper incidenteDeProveedorMapper
    ) {
        this.incidenteDeProveedorRepository = incidenteDeProveedorRepository;
        this.incidenteDeProveedorMapper = incidenteDeProveedorMapper;
    }

    /**
     * Return a {@link Page} of {@link IncidenteDeProveedorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IncidenteDeProveedorDTO> findByCriteria(IncidenteDeProveedorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IncidenteDeProveedor> specification = createSpecification(criteria);
        return incidenteDeProveedorRepository.findAll(specification, page).map(incidenteDeProveedorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IncidenteDeProveedorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IncidenteDeProveedor> specification = createSpecification(criteria);
        return incidenteDeProveedorRepository.count(specification);
    }

    /**
     * Function to convert {@link IncidenteDeProveedorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IncidenteDeProveedor> createSpecification(IncidenteDeProveedorCriteria criteria) {
        Specification<IncidenteDeProveedor> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(IncidenteDeProveedor_.incidente, JoinType.LEFT);
                root.fetch(IncidenteDeProveedor_.servicioDeProveedor, JoinType.LEFT);
                root.fetch(IncidenteDeProveedor_.abiertoPor, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), IncidenteDeProveedor_.id),
                    buildStringSpecification(criteria.getTicketExterno(), IncidenteDeProveedor_.ticketExterno),
                    buildSpecification(criteria.getEstado(), IncidenteDeProveedor_.estado),
                    buildRangeSpecification(criteria.getAbiertoEn(), IncidenteDeProveedor_.abiertoEn),
                    buildRangeSpecification(criteria.getPrimeraRespuestaEn(), IncidenteDeProveedor_.primeraRespuestaEn),
                    buildRangeSpecification(criteria.getResueltoEn(), IncidenteDeProveedor_.resueltoEn),
                    buildSpecification(criteria.getCumplioSla(), IncidenteDeProveedor_.cumplioSla),
                    buildStringSpecification(criteria.getMotivoRechazo(), IncidenteDeProveedor_.motivoRechazo),
                    buildStringSpecification(criteria.getResponsableResuelto(), IncidenteDeProveedor_.responsableResuelto),
                    buildSpecification(criteria.getNivelResuelto(), IncidenteDeProveedor_.nivelResuelto),
                    buildSpecification(criteria.getHuboCobertura(), IncidenteDeProveedor_.huboCobertura),
                    buildStringSpecification(criteria.getNotas(), IncidenteDeProveedor_.notas),
                    buildSpecification(criteria.getIncidenteId(), root ->
                        root.join(IncidenteDeProveedor_.incidente, JoinType.LEFT).get(Incidente_.id)
                    ),
                    buildSpecification(criteria.getServicioDeProveedorId(), root ->
                        root.join(IncidenteDeProveedor_.servicioDeProveedor, JoinType.LEFT).get(ServicioDeProveedor_.id)
                    ),
                    buildSpecification(criteria.getAbiertoPorId(), root ->
                        root.join(IncidenteDeProveedor_.abiertoPor, JoinType.LEFT).get(User_.id)
                    )
                )
            );
        }
        return specification;
    }
}
