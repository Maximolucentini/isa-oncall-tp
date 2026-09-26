package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.ContactoDeProveedor;
import ar.edu.um.isa.oncall.domain.TurnoDeProveedor;
import ar.edu.um.isa.oncall.service.dto.ContactoDeProveedorDTO;
import ar.edu.um.isa.oncall.service.dto.TurnoDeProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TurnoDeProveedor} and its DTO {@link TurnoDeProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface TurnoDeProveedorMapper extends EntityMapper<TurnoDeProveedorDTO, TurnoDeProveedor> {
    @Mapping(target = "contacto", source = "contacto", qualifiedByName = "contactoDeProveedorNombre")
    TurnoDeProveedorDTO toDto(TurnoDeProveedor s);

    @Named("contactoDeProveedorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ContactoDeProveedorDTO toDtoContactoDeProveedorNombre(ContactoDeProveedor contactoDeProveedor);
}
