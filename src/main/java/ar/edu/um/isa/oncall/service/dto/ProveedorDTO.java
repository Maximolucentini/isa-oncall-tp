package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.CoberturaProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.TipoProveedor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.Proveedor} entity.
 */
@Schema(
    description = "Un tercero del que dependemos: nube, pasarela de pagos, correo.\nLa zona horaria y la cobertura son lo que permite resolver quien\nesta de guardia del lado de ellos en un instante dado."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProveedorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    private TipoProveedor tipo;

    @NotNull
    @Size(max = 60)
    private String zonaHoraria;

    @Size(max = 40)
    private String telefonoContacto;

    @Size(max = 120)
    private String emailContacto;

    @Size(max = 500)
    private String urlSoporte;

    @Size(max = 500)
    private String urlEstado;

    @NotNull
    private CoberturaProveedor cobertura;

    @Min(value = 1)
    @Max(value = 10080)
    private Integer slaRespuestaMinutos;

    @NotNull
    private Boolean activo;

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

    public TipoProveedor getTipo() {
        return tipo;
    }

    public void setTipo(TipoProveedor tipo) {
        this.tipo = tipo;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getUrlSoporte() {
        return urlSoporte;
    }

    public void setUrlSoporte(String urlSoporte) {
        this.urlSoporte = urlSoporte;
    }

    public String getUrlEstado() {
        return urlEstado;
    }

    public void setUrlEstado(String urlEstado) {
        this.urlEstado = urlEstado;
    }

    public CoberturaProveedor getCobertura() {
        return cobertura;
    }

    public void setCobertura(CoberturaProveedor cobertura) {
        this.cobertura = cobertura;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProveedorDTO)) {
            return false;
        }

        ProveedorDTO proveedorDTO = (ProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProveedorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", zonaHoraria='" + getZonaHoraria() + "'" +
            ", telefonoContacto='" + getTelefonoContacto() + "'" +
            ", emailContacto='" + getEmailContacto() + "'" +
            ", urlSoporte='" + getUrlSoporte() + "'" +
            ", urlEstado='" + getUrlEstado() + "'" +
            ", cobertura='" + getCobertura() + "'" +
            ", slaRespuestaMinutos=" + getSlaRespuestaMinutos() +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
