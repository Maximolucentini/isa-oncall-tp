package ar.edu.um.isa.oncall.service.mapper;

import ar.edu.um.isa.oncall.domain.Proveedor;
import ar.edu.um.isa.oncall.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proveedor} and its DTO {@link ProveedorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProveedorMapper extends EntityMapper<ProveedorDTO, Proveedor> {}
