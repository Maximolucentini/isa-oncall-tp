package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.IncidenteDeProveedor;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.domain.User;
import ar.edu.um.isa.oncall.service.dto.IncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.IncidenteDeProveedorDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
import ar.edu.um.isa.oncall.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IncidenteDeProveedor} and its DTO {@link IncidenteDeProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface IncidenteDeProveedorMapper extends EntityMapper<IncidenteDeProveedorDTO, IncidenteDeProveedor> {
    @Mapping(target = "incidente", source = "incidente", qualifiedByName = "incidenteTitulo")
    @Mapping(target = "servicioDeProveedor", source = "servicioDeProveedor", qualifiedByName = "servicioDeProveedorNombre")
    @Mapping(target = "abiertoPor", source = "abiertoPor", qualifiedByName = "userLogin")
    IncidenteDeProveedorDTO toDto(IncidenteDeProveedor s);

    @Named("incidenteTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    IncidenteDTO toDtoIncidenteTitulo(Incidente incidente);

    @Named("servicioDeProveedorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ServicioDeProveedorDTO toDtoServicioDeProveedorNombre(ServicioDeProveedor servicioDeProveedor);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
