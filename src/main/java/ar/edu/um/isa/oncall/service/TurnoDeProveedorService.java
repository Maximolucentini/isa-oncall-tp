package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.repository.TurnoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.dto.TurnoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.TurnoDeProveedorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.TurnoDeProveedor}.
 */
@Service
@Transactional
public class TurnoDeProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(TurnoDeProveedorService.class);

    private final TurnoDeProveedorRepository turnoDeProveedorRepository;

    private final TurnoDeProveedorMapper turnoDeProveedorMapper;

    public TurnoDeProveedorService(TurnoDeProveedorRepository turnoDeProveedorRepository, TurnoDeProveedorMapper turnoDeProveedorMapper) {
        this.turnoDeProveedorRepository = turnoDeProveedorRepository;
        this.turnoDeProveedorMapper = turnoDeProveedorMapper;
    }

    /**
     * Save a turnoDeProveedor.
     *
     * @param turnoDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public TurnoDeProveedorDTO save(TurnoDeProveedorDTO turnoDeProveedorDTO) {
        LOG.debug("Request to save TurnoDeProveedor : {}", turnoDeProveedorDTO);
        TurnoDeProveedor turnoDeProveedor = turnoDeProveedorMapper.toEntity(turnoDeProveedorDTO);
        turnoDeProveedor = turnoDeProveedorRepository.save(turnoDeProveedor);
        return turnoDeProveedorMapper.toDto(turnoDeProveedor);
    }

    /**
     * Update a turnoDeProveedor.
     *
     * @param turnoDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public TurnoDeProveedorDTO update(TurnoDeProveedorDTO turnoDeProveedorDTO) {
        LOG.debug("Request to update TurnoDeProveedor : {}", turnoDeProveedorDTO);
        TurnoDeProveedor turnoDeProveedor = turnoDeProveedorMapper.toEntity(turnoDeProveedorDTO);
        turnoDeProveedor = turnoDeProveedorRepository.save(turnoDeProveedor);
        return turnoDeProveedorMapper.toDto(turnoDeProveedor);
    }

    /**
     * Partially update a turnoDeProveedor.
     *
     * @param turnoDeProveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TurnoDeProveedorDTO> partialUpdate(TurnoDeProveedorDTO turnoDeProveedorDTO) {
        LOG.debug("Request to partially update TurnoDeProveedor : {}", turnoDeProveedorDTO);

        return turnoDeProveedorRepository
            .findById(turnoDeProveedorDTO.getId())
            .map(existingTurnoDeProveedor -> {
                turnoDeProveedorMapper.partialUpdate(existingTurnoDeProveedor, turnoDeProveedorDTO);

                return existingTurnoDeProveedor;
            })
            .map(turnoDeProveedorRepository::save)
            .map(turnoDeProveedorMapper::toDto);
    }

    /**
     * Get all the turnoDeProveedors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TurnoDeProveedorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return turnoDeProveedorRepository.findAllWithEagerRelationships(pageable).map(turnoDeProveedorMapper::toDto);
    }

    /**
     * Get one turnoDeProveedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TurnoDeProveedorDTO> findOne(Long id) {
        LOG.debug("Request to get TurnoDeProveedor : {}", id);
        return turnoDeProveedorRepository.findOneWithEagerRelationships(id).map(turnoDeProveedorMapper::toDto);
    }

    /**
     * Delete the turnoDeProveedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TurnoDeProveedor : {}", id);
        turnoDeProveedorRepository.deleteById(id);
    }
}
