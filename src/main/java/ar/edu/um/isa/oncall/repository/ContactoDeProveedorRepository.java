package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactoDeProveedor entity.
 */
@Repository
public interface ContactoDeProveedorRepository extends JpaRepository<ContactoDeProveedor, Long> {
    default Optional<ContactoDeProveedor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContactoDeProveedor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContactoDeProveedor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select contactoDeProveedor from ContactoDeProveedor contactoDeProveedor left join fetch contactoDeProveedor.proveedor",
        countQuery = "select count(contactoDeProveedor) from ContactoDeProveedor contactoDeProveedor"
    )
    Page<ContactoDeProveedor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select contactoDeProveedor from ContactoDeProveedor contactoDeProveedor left join fetch contactoDeProveedor.proveedor")
    List<ContactoDeProveedor> findAllWithToOneRelationships();

    @Query(
        "select contactoDeProveedor from ContactoDeProveedor contactoDeProveedor left join fetch contactoDeProveedor.proveedor where contactoDeProveedor.id =:id"
    )
    Optional<ContactoDeProveedor> findOneWithToOneRelationships(@Param("id") Long id);
}
