package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.repository.ServicioDeProveedorRepository;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.ServicioDeProveedorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.ServicioDeProveedor}.
 */
@Service
@Transactional
public class ServicioDeProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioDeProveedorService.class);

    private final ServicioDeProveedorRepository servicioDeProveedorRepository;

    private final ServicioDeProveedorMapper servicioDeProveedorMapper;

    public ServicioDeProveedorService(
        ServicioDeProveedorRepository servicioDeProveedorRepository,
        ServicioDeProveedorMapper servicioDeProveedorMapper
    ) {
        this.servicioDeProveedorRepository = servicioDeProveedorRepository;
        this.servicioDeProveedorMapper = servicioDeProveedorMapper;
    }

    /**
     * Save a servicioDeProveedor.
     *
     * @param servicioDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioDeProveedorDTO save(ServicioDeProveedorDTO servicioDeProveedorDTO) {
        LOG.debug("Request to save ServicioDeProveedor : {}", servicioDeProveedorDTO);
        ServicioDeProveedor servicioDeProveedor = servicioDeProveedorMapper.toEntity(servicioDeProveedorDTO);
        servicioDeProveedor = servicioDeProveedorRepository.save(servicioDeProveedor);
        return servicioDeProveedorMapper.toDto(servicioDeProveedor);
    }

    /**
     * Update a servicioDeProveedor.
     *
     * @param servicioDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public ServicioDeProveedorDTO update(ServicioDeProveedorDTO servicioDeProveedorDTO) {
        LOG.debug("Request to update ServicioDeProveedor : {}", servicioDeProveedorDTO);
        ServicioDeProveedor servicioDeProveedor = servicioDeProveedorMapper.toEntity(servicioDeProveedorDTO);
        servicioDeProveedor = servicioDeProveedorRepository.save(servicioDeProveedor);
        return servicioDeProveedorMapper.toDto(servicioDeProveedor);
    }

    /**
     * Partially update a servicioDeProveedor.
     *
     * @param servicioDeProveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServicioDeProveedorDTO> partialUpdate(ServicioDeProveedorDTO servicioDeProveedorDTO) {
        LOG.debug("Request to partially update ServicioDeProveedor : {}", servicioDeProveedorDTO);

        return servicioDeProveedorRepository
            .findById(servicioDeProveedorDTO.getId())
            .map(existingServicioDeProveedor -> {
                servicioDeProveedorMapper.partialUpdate(existingServicioDeProveedor, servicioDeProveedorDTO);

                return existingServicioDeProveedor;
            })
            .map(servicioDeProveedorRepository::save)
            .map(servicioDeProveedorMapper::toDto);
    }

    /**
     * Get all the servicioDeProveedors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServicioDeProveedorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return servicioDeProveedorRepository.findAllWithEagerRelationships(pageable).map(servicioDeProveedorMapper::toDto);
    }

    /**
     * Get one servicioDeProveedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServicioDeProveedorDTO> findOne(Long id) {
        LOG.debug("Request to get ServicioDeProveedor : {}", id);
        return servicioDeProveedorRepository.findOneWithEagerRelationships(id).map(servicioDeProveedorMapper::toDto);
    }

    /**
     * Delete the servicioDeProveedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ServicioDeProveedor : {}", id);
        servicioDeProveedorRepository.deleteById(id);
    }
}
