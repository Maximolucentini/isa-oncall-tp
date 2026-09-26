package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.TurnoDeProveedor} entity.
 */
@Schema(
    description = "La guardia del proveedor. Entidad propia y no una Rotacion, porque\nuna rotacion pertenece siempre a un Equipo y esta no tiene ninguno."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurnoDeProveedorDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant desde;

    @NotNull
    private Instant hasta;

    @NotNull
    private NivelGuardia nivel;

    @NotNull
    private Boolean esReemplazo;

    @Size(max = 500)
    private String nota;

    @NotNull
    private ContactoDeProveedorDTO contacto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDesde() {
        return desde;
    }

    public void setDesde(Instant desde) {
        this.desde = desde;
    }

    public Instant getHasta() {
        return hasta;
    }

    public void setHasta(Instant hasta) {
        this.hasta = hasta;
    }

    public NivelGuardia getNivel() {
        return nivel;
    }

    public void setNivel(NivelGuardia nivel) {
        this.nivel = nivel;
    }

    public Boolean getEsReemplazo() {
        return esReemplazo;
    }

    public void setEsReemplazo(Boolean esReemplazo) {
        this.esReemplazo = esReemplazo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public ContactoDeProveedorDTO getContacto() {
        return contacto;
    }

    public void setContacto(ContactoDeProveedorDTO contacto) {
        this.contacto = contacto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurnoDeProveedorDTO)) {
            return false;
        }

        TurnoDeProveedorDTO turnoDeProveedorDTO = (TurnoDeProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, turnoDeProveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnoDeProveedorDTO{" +
            "id=" + getId() +
            ", desde='" + getDesde() + "'" +
            ", hasta='" + getHasta() + "'" +
            ", nivel='" + getNivel() + "'" +
            ", esReemplazo='" + getEsReemplazo() + "'" +
            ", nota='" + getNota() + "'" +
            ", contacto=" + getContacto() +
            "}";
    }
}
