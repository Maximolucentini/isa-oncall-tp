package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import ar.edu.um.isa.oncall.repository.IncidenteDeProveedorRepository;
import ar.edu.um.isa.oncall.service.dto.IncidenteDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.IncidenteDeProveedorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.IncidenteDeProveedor}.
 */
@Service
@Transactional
public class IncidenteDeProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(IncidenteDeProveedorService.class);

    private final IncidenteDeProveedorRepository incidenteDeProveedorRepository;

    private final IncidenteDeProveedorMapper incidenteDeProveedorMapper;

    public IncidenteDeProveedorService(
        IncidenteDeProveedorRepository incidenteDeProveedorRepository,
        IncidenteDeProveedorMapper incidenteDeProveedorMapper
    ) {
        this.incidenteDeProveedorRepository = incidenteDeProveedorRepository;
        this.incidenteDeProveedorMapper = incidenteDeProveedorMapper;
    }

    /**
     * Save a incidenteDeProveedor.
     *
     * @param incidenteDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public IncidenteDeProveedorDTO save(IncidenteDeProveedorDTO incidenteDeProveedorDTO) {
        LOG.debug("Request to save IncidenteDeProveedor : {}", incidenteDeProveedorDTO);
        IncidenteDeProveedor incidenteDeProveedor = incidenteDeProveedorMapper.toEntity(incidenteDeProveedorDTO);
        incidenteDeProveedor = incidenteDeProveedorRepository.save(incidenteDeProveedor);
        return incidenteDeProveedorMapper.toDto(incidenteDeProveedor);
    }

    /**
     * Update a incidenteDeProveedor.
     *
     * @param incidenteDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public IncidenteDeProveedorDTO update(IncidenteDeProveedorDTO incidenteDeProveedorDTO) {
        LOG.debug("Request to update IncidenteDeProveedor : {}", incidenteDeProveedorDTO);
        IncidenteDeProveedor incidenteDeProveedor = incidenteDeProveedorMapper.toEntity(incidenteDeProveedorDTO);
        incidenteDeProveedor = incidenteDeProveedorRepository.save(incidenteDeProveedor);
        return incidenteDeProveedorMapper.toDto(incidenteDeProveedor);
    }

    /**
     * Partially update a incidenteDeProveedor.
     *
     * @param incidenteDeProveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IncidenteDeProveedorDTO> partialUpdate(IncidenteDeProveedorDTO incidenteDeProveedorDTO) {
        LOG.debug("Request to partially update IncidenteDeProveedor : {}", incidenteDeProveedorDTO);

        return incidenteDeProveedorRepository
            .findById(incidenteDeProveedorDTO.getId())
            .map(existingIncidenteDeProveedor -> {
                incidenteDeProveedorMapper.partialUpdate(existingIncidenteDeProveedor, incidenteDeProveedorDTO);

                return existingIncidenteDeProveedor;
            })
            .map(incidenteDeProveedorRepository::save)
            .map(incidenteDeProveedorMapper::toDto);
    }

    /**
     * Get all the incidenteDeProveedors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IncidenteDeProveedorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return incidenteDeProveedorRepository.findAllWithEagerRelationships(pageable).map(incidenteDeProveedorMapper::toDto);
    }

    /**
     * Get one incidenteDeProveedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IncidenteDeProveedorDTO> findOne(Long id) {
        LOG.debug("Request to get IncidenteDeProveedor : {}", id);
        return incidenteDeProveedorRepository.findOneWithEagerRelationships(id).map(incidenteDeProveedorMapper::toDto);
    }

    /**
     * Delete the incidenteDeProveedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IncidenteDeProveedor : {}", id);
        incidenteDeProveedorRepository.deleteById(id);
    }
}
