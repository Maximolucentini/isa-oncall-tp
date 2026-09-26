package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.service.dto.ContactoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactoDeProveedor} and its DTO {@link ContactoDeProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactoDeProveedorMapper extends EntityMapper<ContactoDeProveedorDTO, ContactoDeProveedor> {
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "proveedorNombre")
    ContactoDeProveedorDTO toDto(ContactoDeProveedor s);

    @Named("proveedorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProveedorDTO toDtoProveedorNombre(Proveedor proveedor);
}
