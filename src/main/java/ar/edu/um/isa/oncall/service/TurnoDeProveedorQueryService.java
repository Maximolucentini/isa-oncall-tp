package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.*; // for static metamodels
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.repository.TurnoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.criteria.TurnoDeProveedorCriteria;
import ar.edu.um.isa.oncall.service.dto.TurnoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.TurnoDeProveedorMapper;
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
 * Service for executing complex queries for {@link TurnoDeProveedor} entities in the database.
 * The main input is a {@link TurnoDeProveedorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TurnoDeProveedorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TurnoDeProveedorQueryService extends QueryService<TurnoDeProveedor> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoDeProveedorQueryService.class);

    private final TurnoDeProveedorRepository turnoDeProveedorRepository;

    private final TurnoDeProveedorMapper turnoDeProveedorMapper;

    public TurnoDeProveedorQueryService(
        TurnoDeProveedorRepository turnoDeProveedorRepository,
        TurnoDeProveedorMapper turnoDeProveedorMapper
    ) {
        this.turnoDeProveedorRepository = turnoDeProveedorRepository;
        this.turnoDeProveedorMapper = turnoDeProveedorMapper;
    }

    /**
     * Return a {@link Page} of {@link TurnoDeProveedorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TurnoDeProveedorDTO> findByCriteria(TurnoDeProveedorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TurnoDeProveedor> specification = createSpecification(criteria);
        return turnoDeProveedorRepository.findAll(specification, page).map(turnoDeProveedorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TurnoDeProveedorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TurnoDeProveedor> specification = createSpecification(criteria);
        return turnoDeProveedorRepository.count(specification);
    }

    /**
     * Function to convert {@link TurnoDeProveedorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TurnoDeProveedor> createSpecification(TurnoDeProveedorCriteria criteria) {
        Specification<TurnoDeProveedor> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TurnoDeProveedor_.contacto, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TurnoDeProveedor_.id),
                    buildRangeSpecification(criteria.getDesde(), TurnoDeProveedor_.desde),
                    buildRangeSpecification(criteria.getHasta(), TurnoDeProveedor_.hasta),
                    buildSpecification(criteria.getNivel(), TurnoDeProveedor_.nivel),
                    buildSpecification(criteria.getEsReemplazo(), TurnoDeProveedor_.esReemplazo),
                    buildStringSpecification(criteria.getNota(), TurnoDeProveedor_.nota),
                    buildSpecification(criteria.getContactoId(), root ->
                        root.join(TurnoDeProveedor_.contacto, JoinType.LEFT).get(ContactoDeProveedor_.id)
                    )
                )
            );
        }
        return specification;
    }
}
