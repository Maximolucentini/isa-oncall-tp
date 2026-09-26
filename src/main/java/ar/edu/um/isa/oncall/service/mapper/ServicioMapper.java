package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Equipo;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Servicio;
import ar.edu.um.isa.oncall.domain.ServicioDeProveedor;
import ar.edu.um.isa.oncall.service.dto.EquipoDTO;
import ar.edu.um.isa.oncall.service.dto.IncidenteDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDTO;
import ar.edu.um.isa.oncall.service.dto.ServicioDeProveedorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Servicio} and its DTO {@link ServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioMapper extends EntityMapper<ServicioDTO, Servicio> {
    @Mapping(target = "equipo", source = "equipo", qualifiedByName = "equipoNombre")
    @Mapping(target = "incidentes", source = "incidentes", qualifiedByName = "incidenteIdSet")
    @Mapping(target = "proveedorDelQueDependes", source = "proveedorDelQueDependes", qualifiedByName = "servicioDeProveedorIdSet")
    ServicioDTO toDto(Servicio s);

    @Mapping(target = "incidentes", ignore = true)
    @Mapping(target = "removeIncidente", ignore = true)
    @Mapping(target = "proveedorDelQueDependes", ignore = true)
    @Mapping(target = "removeProveedorDelQueDepende", ignore = true)
    Servicio toEntity(ServicioDTO servicioDTO);

    @Named("equipoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EquipoDTO toDtoEquipoNombre(Equipo equipo);

    @Named("incidenteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IncidenteDTO toDtoIncidenteId(Incidente incidente);

    @Named("incidenteIdSet")
    default Set<IncidenteDTO> toDtoIncidenteIdSet(Set<Incidente> incidente) {
        return incidente.stream().map(this::toDtoIncidenteId).collect(Collectors.toSet());
    }

    @Named("servicioDeProveedorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicioDeProveedorDTO toDtoServicioDeProveedorId(ServicioDeProveedor servicioDeProveedor);

    @Named("servicioDeProveedorIdSet")
    default Set<ServicioDeProveedorDTO> toDtoServicioDeProveedorIdSet(Set<ServicioDeProveedor> servicioDeProveedor) {
        return servicioDeProveedor.stream().map(this::toDtoServicioDeProveedorId).collect(Collectors.toSet());
    }
}
