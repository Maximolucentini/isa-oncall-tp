package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncidenteDeProveedor entity.
 */
@Repository
public interface IncidenteDeProveedorRepository
    extends JpaRepository<IncidenteDeProveedor, Long>, JpaSpecificationExecutor<IncidenteDeProveedor>
{
    @Query(
        "select incidenteDeProveedor from IncidenteDeProveedor incidenteDeProveedor where incidenteDeProveedor.abiertoPor.login = ?#{authentication.name}"
    )
    List<IncidenteDeProveedor> findByAbiertoPorIsCurrentUser();

    default Optional<IncidenteDeProveedor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<IncidenteDeProveedor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<IncidenteDeProveedor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select incidenteDeProveedor from IncidenteDeProveedor incidenteDeProveedor left join fetch incidenteDeProveedor.incidente left join fetch incidenteDeProveedor.servicioDeProveedor left join fetch incidenteDeProveedor.abiertoPor",
        countQuery = "select count(incidenteDeProveedor) from IncidenteDeProveedor incidenteDeProveedor"
    )
    Page<IncidenteDeProveedor> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select incidenteDeProveedor from IncidenteDeProveedor incidenteDeProveedor left join fetch incidenteDeProveedor.incidente left join fetch incidenteDeProveedor.servicioDeProveedor left join fetch incidenteDeProveedor.abiertoPor"
    )
    List<IncidenteDeProveedor> findAllWithToOneRelationships();

    @Query(
        "select incidenteDeProveedor from IncidenteDeProveedor incidenteDeProveedor left join fetch incidenteDeProveedor.incidente left join fetch incidenteDeProveedor.servicioDeProveedor left join fetch incidenteDeProveedor.abiertoPor where incidenteDeProveedor.id =:id"
    )
    Optional<IncidenteDeProveedor> findOneWithToOneRelationships(@Param("id") Long id);
}
