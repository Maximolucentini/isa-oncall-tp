package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ServicioDeProveedorRepositoryWithBagRelationshipsImpl implements ServicioDeProveedorRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SERVICIODEPROVEEDORS_PARAMETER = "servicioDeProveedors";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ServicioDeProveedor> fetchBagRelationships(Optional<ServicioDeProveedor> servicioDeProveedor) {
        return servicioDeProveedor.map(this::fetchServicioInternos);
    }

    @Override
    public Page<ServicioDeProveedor> fetchBagRelationships(Page<ServicioDeProveedor> servicioDeProveedors) {
        return new PageImpl<>(
            fetchBagRelationships(servicioDeProveedors.getContent()),
            servicioDeProveedors.getPageable(),
            servicioDeProveedors.getTotalElements()
        );
    }

    @Override
    public List<ServicioDeProveedor> fetchBagRelationships(List<ServicioDeProveedor> servicioDeProveedors) {
        return Optional.of(servicioDeProveedors).map(this::fetchServicioInternos).orElse(List.of());
    }

    ServicioDeProveedor fetchServicioInternos(ServicioDeProveedor result) {
        return entityManager
            .createQuery(
                "select servicioDeProveedor from ServicioDeProveedor servicioDeProveedor left join fetch servicioDeProveedor.servicioInternos where servicioDeProveedor.id = :id",
                ServicioDeProveedor.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ServicioDeProveedor> fetchServicioInternos(List<ServicioDeProveedor> servicioDeProveedors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, servicioDeProveedors.size()).forEach(index -> order.put(servicioDeProveedors.get(index).getId(), index));
        List<ServicioDeProveedor> result = entityManager
            .createQuery(
                "select servicioDeProveedor from ServicioDeProveedor servicioDeProveedor left join fetch servicioDeProveedor.servicioInternos where servicioDeProveedor in :servicioDeProveedors",
                ServicioDeProveedor.class
            )
            .setParameter(SERVICIODEPROVEEDORS_PARAMETER, servicioDeProveedors)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
