package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.repository.ServicioDeProveedorRepository;
import ar.edu.um.isa.oncall.service.criteria.ServicioDeProveedorCriteria;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioDeProveedorMapper;
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
 * Service for executing complex queries for {@link ServicioDeProveedor} entities in the database.
 * The main input is a {@link ServicioDeProveedorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServicioDeProveedorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicioDeProveedorQueryService extends QueryService<ServicioDeProveedor> {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioDeProveedorQueryService.class);

    private final ServicioDeProveedorRepository servicioDeProveedorRepository;

    private final ServicioDeProveedorMapper servicioDeProveedorMapper;

    public ServicioDeProveedorQueryService(
        ServicioDeProveedorRepository servicioDeProveedorRepository,
        ServicioDeProveedorMapper servicioDeProveedorMapper
    ) {
        this.servicioDeProveedorRepository = servicioDeProveedorRepository;
        this.servicioDeProveedorMapper = servicioDeProveedorMapper;
    }

    /**
     * Return a {@link Page} of {@link ServicioDeProveedorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicioDeProveedorDTO> findByCriteria(ServicioDeProveedorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServicioDeProveedor> specification = createSpecification(criteria);
        return servicioDeProveedorRepository
            .fetchBagRelationships(servicioDeProveedorRepository.findAll(specification, page))
            .map(servicioDeProveedorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServicioDeProveedorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ServicioDeProveedor> specification = createSpecification(criteria);
        return servicioDeProveedorRepository.count(specification);
    }

    /**
     * Function to convert {@link ServicioDeProveedorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServicioDeProveedor> createSpecification(ServicioDeProveedorCriteria criteria) {
        Specification<ServicioDeProveedor> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(ServicioDeProveedor_.proveedor, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ServicioDeProveedor_.id),
                    buildStringSpecification(criteria.getNombre(), ServicioDeProveedor_.nombre),
                    buildStringSpecification(criteria.getIdentificadorExterno(), ServicioDeProveedor_.identificadorExterno),
                    buildStringSpecification(criteria.getDescripcion(), ServicioDeProveedor_.descripcion),
                    buildRangeSpecification(criteria.getSlaRespuestaMinutos(), ServicioDeProveedor_.slaRespuestaMinutos),
                    buildSpecification(criteria.getActivo(), ServicioDeProveedor_.activo),
                    buildSpecification(criteria.getProveedorId(), root ->
                        root.join(ServicioDeProveedor_.proveedor, JoinType.LEFT).get(Proveedor_.id)
                    ),
                    buildSpecification(criteria.getServicioInternoId(), root ->
                        root.join(ServicioDeProveedor_.servicioInternos, JoinType.LEFT).get(Servicio_.id)
                    ),
                    buildSpecification(criteria.getTicketId(), root ->
                        root.join(ServicioDeProveedor_.tickets, JoinType.LEFT).get(IncidenteDeProveedor_.id)
                    )
                )
            );
        }
        return specification;
    }
}
