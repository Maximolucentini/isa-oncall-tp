package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ServicioDeProveedor} entity.
 */
@Schema(description = "Componente concreto que consumimos de ese proveedor.\nEl SLA propio, si esta cargado, pisa al del proveedor.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioDeProveedorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @Size(max = 200)
    private String identificadorExterno;

    @Size(max = 500)
    private String descripcion;

    @Min(value = 1)
    @Max(value = 10080)
    private Integer slaRespuestaMinutos;

    @NotNull
    private Boolean activo;

    @NotNull
    private ProveedorDTO proveedor;

    private Set<ServicioDTO> servicioInternos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSlaRespuestaMinutos() {
        return slaRespuestaMinutos;
    }

    public void setSlaRespuestaMinutos(Integer slaRespuestaMinutos) {
        this.slaRespuestaMinutos = slaRespuestaMinutos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ProveedorDTO getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorDTO proveedor) {
        this.proveedor = proveedor;
    }

    public Set<ServicioDTO> getServicioInternos() {
        return servicioInternos;
    }

    public void setServicioInternos(Set<ServicioDTO> servicioInternos) {
        this.servicioInternos = servicioInternos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioDeProveedorDTO)) {
            return false;
        }

        ServicioDeProveedorDTO servicioDeProveedorDTO = (ServicioDeProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioDeProveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDeProveedorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", slaRespuestaMinutos=" + getSlaRespuestaMinutos() +
            ", activo='" + getActivo() + "'" +
            ", proveedor=" + getProveedor() +
            ", servicioInternos=" + getServicioInternos() +
            "}";
    }
}
