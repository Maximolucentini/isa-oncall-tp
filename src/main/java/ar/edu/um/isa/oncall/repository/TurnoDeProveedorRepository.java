package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TurnoDeProveedor entity.
 */
@Repository
public interface TurnoDeProveedorRepository extends JpaRepository<TurnoDeProveedor, Long>, JpaSpecificationExecutor<TurnoDeProveedor> {
    default Optional<TurnoDeProveedor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TurnoDeProveedor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TurnoDeProveedor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select turnoDeProveedor from TurnoDeProveedor turnoDeProveedor left join fetch turnoDeProveedor.contacto",
        countQuery = "select count(turnoDeProveedor) from TurnoDeProveedor turnoDeProveedor"
    )
    Page<TurnoDeProveedor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select turnoDeProveedor from TurnoDeProveedor turnoDeProveedor left join fetch turnoDeProveedor.contacto")
    List<TurnoDeProveedor> findAllWithToOneRelationships();

    @Query(
        "select turnoDeProveedor from TurnoDeProveedor turnoDeProveedor left join fetch turnoDeProveedor.contacto where turnoDeProveedor.id =:id"
    )
    Optional<TurnoDeProveedor> findOneWithToOneRelationships(@Param("id") Long id);
}
