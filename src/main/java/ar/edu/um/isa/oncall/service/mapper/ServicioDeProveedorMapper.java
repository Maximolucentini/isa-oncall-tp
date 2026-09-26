package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.service.dto.ProveedorDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServicioDeProveedor} and its DTO {@link ServicioDeProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioDeProveedorMapper extends EntityMapper<ServicioDeProveedorDTO, ServicioDeProveedor> {
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "proveedorNombre")
    @Mapping(target = "servicioInternos", source = "servicioInternos", qualifiedByName = "servicioNombreSet")
    ServicioDeProveedorDTO toDto(ServicioDeProveedor s);

    @Mapping(target = "removeServicioInterno", ignore = true)
    ServicioDeProveedor toEntity(ServicioDeProveedorDTO servicioDeProveedorDTO);

    @Named("proveedorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProveedorDTO toDtoProveedorNombre(Proveedor proveedor);

    @Named("servicioNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDTO toDtoServicioNombre(Servicio servicio);

    @Named("servicioNombreSet")
    default Set<ServicioDTO> toDtoServicioNombreSet(Set<Servicio> servicio) {
        return servicio.stream().map(this::toDtoServicioNombre).collect(Collectors.toSet());
    }
}
