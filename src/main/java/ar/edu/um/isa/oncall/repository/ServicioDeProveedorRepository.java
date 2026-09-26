package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServicioDeProveedor entity.
 *
 * When extending this class, extend ServicioDeProveedorRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ServicioDeProveedorRepository
    extends
        ServicioDeProveedorRepositoryWithBagRelationships,
        JpaRepository<ServicioDeProveedor, Long>,
        JpaSpecificationExecutor<ServicioDeProveedor>
{
    default Optional<ServicioDeProveedor> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<ServicioDeProveedor> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<ServicioDeProveedor> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select servicioDeProveedor from ServicioDeProveedor servicioDeProveedor left join fetch servicioDeProveedor.proveedor",
        countQuery = "select count(servicioDeProveedor) from ServicioDeProveedor servicioDeProveedor"
    )
    Page<ServicioDeProveedor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select servicioDeProveedor from ServicioDeProveedor servicioDeProveedor left join fetch servicioDeProveedor.proveedor")
    List<ServicioDeProveedor> findAllWithToOneRelationships();

    @Query(
        "select servicioDeProveedor from ServicioDeProveedor servicioDeProveedor left join fetch servicioDeProveedor.proveedor where servicioDeProveedor.id =:id"
    )
    Optional<ServicioDeProveedor> findOneWithToOneRelationships(@Param("id") Long id);
}
