package ar.edu.um.isa.oncall.service.dto;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoTicketProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.isa.oncall.domain.IncidenteDeProveedor} entity.
 */
@Schema(
    description = "Ticket del lado del proveedor. Puede nacer sin incidente propio:\nuna degradacion detectada que todavia no nos afecto.\nLos campos *Resuelto congelan lo que respondio la regla C al abrirlo."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncidenteDeProveedorDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String ticketExterno;

    @NotNull
    private EstadoTicketProveedor estado;

    @NotNull
    private Instant abiertoEn;

    private Instant primeraRespuestaEn;

    private Instant resueltoEn;

    private Boolean cumplioSla;

    @Size(max = 1000)
    private String motivoRechazo;

    @Size(max = 100)
    private String responsableResuelto;

    private NivelGuardia nivelResuelto;

    private Boolean huboCobertura;

    @Size(max = 4000)
    private String notas;

    private IncidenteDTO incidente;

    @NotNull
    private ServicioDeProveedorDTO servicioDeProveedor;

    private UserDTO abiertoPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketExterno() {
        return ticketExterno;
    }

    public void setTicketExterno(String ticketExterno) {
        this.ticketExterno = ticketExterno;
    }

    public EstadoTicketProveedor getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicketProveedor estado) {
        this.estado = estado;
    }

    public Instant getAbiertoEn() {
        return abiertoEn;
    }

    public void setAbiertoEn(Instant abiertoEn) {
        this.abiertoEn = abiertoEn;
    }

    public Instant getPrimeraRespuestaEn() {
        return primeraRespuestaEn;
    }

    public void setPrimeraRespuestaEn(Instant primeraRespuestaEn) {
        this.primeraRespuestaEn = primeraRespuestaEn;
    }

    public Instant getResueltoEn() {
        return resueltoEn;
    }

    public void setResueltoEn(Instant resueltoEn) {
        this.resueltoEn = resueltoEn;
    }

    public Boolean getCumplioSla() {
        return cumplioSla;
    }

    public void setCumplioSla(Boolean cumplioSla) {
        this.cumplioSla = cumplioSla;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getResponsableResuelto() {
        return responsableResuelto;
    }

    public void setResponsableResuelto(String responsableResuelto) {
        this.responsableResuelto = responsableResuelto;
    }

    public NivelGuardia getNivelResuelto() {
        return nivelResuelto;
    }

    public void setNivelResuelto(NivelGuardia nivelResuelto) {
        this.nivelResuelto = nivelResuelto;
    }

    public Boolean getHuboCobertura() {
        return huboCobertura;
    }

    public void setHuboCobertura(Boolean huboCobertura) {
        this.huboCobertura = huboCobertura;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public IncidenteDTO getIncidente() {
        return incidente;
    }

    public void setIncidente(IncidenteDTO incidente) {
        this.incidente = incidente;
    }

    public ServicioDeProveedorDTO getServicioDeProveedor() {
        return servicioDeProveedor;
    }

    public void setServicioDeProveedor(ServicioDeProveedorDTO servicioDeProveedor) {
        this.servicioDeProveedor = servicioDeProveedor;
    }

    public UserDTO getAbiertoPor() {
        return abiertoPor;
    }

    public void setAbiertoPor(UserDTO abiertoPor) {
        this.abiertoPor = abiertoPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncidenteDeProveedorDTO)) {
            return false;
        }

        IncidenteDeProveedorDTO incidenteDeProveedorDTO = (IncidenteDeProveedorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, incidenteDeProveedorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncidenteDeProveedorDTO{" +
            "id=" + getId() +
            ", ticketExterno='" + getTicketExterno() + "'" +
            ", estado='" + getEstado() + "'" +
            ", abiertoEn='" + getAbiertoEn() + "'" +
            ", primeraRespuestaEn='" + getPrimeraRespuestaEn() + "'" +
            ", resueltoEn='" + getResueltoEn() + "'" +
            ", cumplioSla='" + getCumplioSla() + "'" +
            ", motivoRechazo='" + getMotivoRechazo() + "'" +
            ", responsableResuelto='" + getResponsableResuelto() + "'" +
            ", nivelResuelto='" + getNivelResuelto() + "'" +
            ", huboCobertura='" + getHuboCobertura() + "'" +
            ", notas='" + getNotas() + "'" +
            ", incidente=" + getIncidente() +
            ", servicioDeProveedor=" + getServicioDeProveedor() +
            ", abiertoPor=" + getAbiertoPor() +
            "}";
    }
}
