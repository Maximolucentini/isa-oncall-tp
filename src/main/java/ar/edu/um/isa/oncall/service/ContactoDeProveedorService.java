package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.repository.ContactoDeProveedorRepository;
import ar.edu.um.isa.oncall.service.dto.ContactoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.mapper.ContactoDeProveedorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.ContactoDeProveedor}.
 */
@Service
@Transactional
public class ContactoDeProveedorService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactoDeProveedorService.class);

    private final ContactoDeProveedorRepository contactoDeProveedorRepository;

    private final ContactoDeProveedorMapper contactoDeProveedorMapper;

    public ContactoDeProveedorService(
        ContactoDeProveedorRepository contactoDeProveedorRepository,
        ContactoDeProveedorMapper contactoDeProveedorMapper
    ) {
        this.contactoDeProveedorRepository = contactoDeProveedorRepository;
        this.contactoDeProveedorMapper = contactoDeProveedorMapper;
    }

    /**
     * Save a contactoDeProveedor.
     *
     * @param contactoDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactoDeProveedorDTO save(ContactoDeProveedorDTO contactoDeProveedorDTO) {
        LOG.debug("Request to save ContactoDeProveedor : {}", contactoDeProveedorDTO);
        ContactoDeProveedor contactoDeProveedor = contactoDeProveedorMapper.toEntity(contactoDeProveedorDTO);
        contactoDeProveedor = contactoDeProveedorRepository.save(contactoDeProveedor);
        return contactoDeProveedorMapper.toDto(contactoDeProveedor);
    }

    /**
     * Update a contactoDeProveedor.
     *
     * @param contactoDeProveedorDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactoDeProveedorDTO update(ContactoDeProveedorDTO contactoDeProveedorDTO) {
        LOG.debug("Request to update ContactoDeProveedor : {}", contactoDeProveedorDTO);
        ContactoDeProveedor contactoDeProveedor = contactoDeProveedorMapper.toEntity(contactoDeProveedorDTO);
        contactoDeProveedor = contactoDeProveedorRepository.save(contactoDeProveedor);
        return contactoDeProveedorMapper.toDto(contactoDeProveedor);
    }

    /**
     * Partially update a contactoDeProveedor.
     *
     * @param contactoDeProveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactoDeProveedorDTO> partialUpdate(ContactoDeProveedorDTO contactoDeProveedorDTO) {
        LOG.debug("Request to partially update ContactoDeProveedor : {}", contactoDeProveedorDTO);

        return contactoDeProveedorRepository
            .findById(contactoDeProveedorDTO.getId())
            .map(existingContactoDeProveedor -> {
                contactoDeProveedorMapper.partialUpdate(existingContactoDeProveedor, contactoDeProveedorDTO);

                return existingContactoDeProveedor;
            })
            .map(contactoDeProveedorRepository::save)
            .map(contactoDeProveedorMapper::toDto);
    }

    /**
     * Get all the contactoDeProveedors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactoDeProveedorDTO> findAll() {
        LOG.debug("Request to get all ContactoDeProveedors");
        return contactoDeProveedorRepository
            .findAll()
            .stream()
            .map(contactoDeProveedorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the contactoDeProveedors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContactoDeProveedorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contactoDeProveedorRepository.findAllWithEagerRelationships(pageable).map(contactoDeProveedorMapper::toDto);
    }

    /**
     * Get one contactoDeProveedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactoDeProveedorDTO> findOne(Long id) {
        LOG.debug("Request to get ContactoDeProveedor : {}", id);
        return contactoDeProveedorRepository.findOneWithEagerRelationships(id).map(contactoDeProveedorMapper::toDto);
    }

    /**
     * Delete the contactoDeProveedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContactoDeProveedor : {}", id);
        contactoDeProveedorRepository.deleteById(id);
    }
}
