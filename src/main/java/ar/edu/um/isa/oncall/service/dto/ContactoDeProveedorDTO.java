package ar.edu.um.isa.oncall.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.ContactoDeProveedor} entity.
 */
@Schema(description = "Persona del lado del proveedor que puede estar de guardia.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactoDeProveedorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    @Size(max = 120)
    private String email;

    @Size(max = 40)
    private String telefono;

    @Size(max = 60)
    private String rol;

    @NotNull
    private Boolean activo;

    @NotNull
    private ProveedorDTO proveedor;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactoDeProveedorDTO)) {
            return false;
        }

        ContactoDeProveedorDTO contactoDeProveedorDTO = (ContactoDeProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactoDeProveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactoDeProveedorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", rol='" + getRol() + "'" +
            ", activo='" + getActivo() + "'" +
            ", proveedor=" + getProveedor() +
            "}";
    }
}
