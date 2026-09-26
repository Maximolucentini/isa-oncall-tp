package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.Proveedor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Proveedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {}
