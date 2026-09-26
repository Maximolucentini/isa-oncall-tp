package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ServicioDeProveedorRepositoryWithBagRelationships {
    Optional<ServicioDeProveedor> fetchBagRelationships(Optional<ServicioDeProveedor> servicioDeProveedor);

    List<ServicioDeProveedor> fetchBagRelationships(List<ServicioDeProveedor> servicioDeProveedors);

    Page<ServicioDeProveedor> fetchBagRelationships(Page<ServicioDeProveedor> servicioDeProveedors);
}
