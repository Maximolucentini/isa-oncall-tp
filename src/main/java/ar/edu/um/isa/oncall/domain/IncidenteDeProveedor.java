package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.EstadoTicketProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.NivelGuardia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Ticket del lado del proveedor. Puede nacer sin incidente propio:
 * una degradacion detectada que todavia no nos afecto.
 * Los campos *Resuelto congelan lo que respondio la regla C al abrirlo.
 */
@Entity
@Table(name = "incidente_de_proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncidenteDeProveedor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "ticket_externo", length = 100)
    private String ticketExterno;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTicketProveedor estado;

    @NotNull
    @Column(name = "abierto_en", nullable = false)
    private Instant abiertoEn;

    @Column(name = "primera_respuesta_en")
    private Instant primeraRespuestaEn;

    @Column(name = "resuelto_en")
    private Instant resueltoEn;

    @Column(name = "cumplio_sla")
    private Boolean cumplioSla;

    @Size(max = 1000)
    @Column(name = "motivo_rechazo", length = 1000)
    private String motivoRechazo;

    @Size(max = 100)
    @Column(name = "responsable_resuelto", length = 100)
    private String responsableResuelto;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_resuelto")
    private NivelGuardia nivelResuelto;

    @Column(name = "hubo_cobertura")
    private Boolean huboCobertura;

    @Size(max = 4000)
    @Column(name = "notas", length = 4000)
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "comandante", "servicios", "postmortem", "alertas", "eventos", "notificacions", "incidenteDeProveedors" },
        allowSetters = true
    )
    private Incidente incidente;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "proveedor", "servicioInternos", "tickets" }, allowSetters = true)
    private ServicioDeProveedor servicioDeProveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    private User abiertoPor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncidenteDeProveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketExterno() {
        return this.ticketExterno;
    }

    public IncidenteDeProveedor ticketExterno(String ticketExterno) {
        this.setTicketExterno(ticketExterno);
        return this;
    }

    public void setTicketExterno(String ticketExterno) {
        this.ticketExterno = ticketExterno;
    }

    public EstadoTicketProveedor getEstado() {
        return this.estado;
    }

    public IncidenteDeProveedor estado(EstadoTicketProveedor estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoTicketProveedor estado) {
        this.estado = estado;
    }

    public Instant getAbiertoEn() {
        return this.abiertoEn;
    }

    public IncidenteDeProveedor abiertoEn(Instant abiertoEn) {
        this.setAbiertoEn(abiertoEn);
        return this;
    }

    public void setAbiertoEn(Instant abiertoEn) {
        this.abiertoEn = abiertoEn;
    }

    public Instant getPrimeraRespuestaEn() {
        return this.primeraRespuestaEn;
    }

    public IncidenteDeProveedor primeraRespuestaEn(Instant primeraRespuestaEn) {
        this.setPrimeraRespuestaEn(primeraRespuestaEn);
        return this;
    }

    public void setPrimeraRespuestaEn(Instant primeraRespuestaEn) {
        this.primeraRespuestaEn = primeraRespuestaEn;
    }

    public Instant getResueltoEn() {
        return this.resueltoEn;
    }

    public IncidenteDeProveedor resueltoEn(Instant resueltoEn) {
        this.setResueltoEn(resueltoEn);
        return this;
    }

    public void setResueltoEn(Instant resueltoEn) {
        this.resueltoEn = resueltoEn;
    }

    public Boolean getCumplioSla() {
        return this.cumplioSla;
    }

    public IncidenteDeProveedor cumplioSla(Boolean cumplioSla) {
        this.setCumplioSla(cumplioSla);
        return this;
    }

    public void setCumplioSla(Boolean cumplioSla) {
        this.cumplioSla = cumplioSla;
    }

    public String getMotivoRechazo() {
        return this.motivoRechazo;
    }

    public IncidenteDeProveedor motivoRechazo(String motivoRechazo) {
        this.setMotivoRechazo(motivoRechazo);
        return this;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getResponsableResuelto() {
        return this.responsableResuelto;
    }

    public IncidenteDeProveedor responsableResuelto(String responsableResuelto) {
        this.setResponsableResuelto(responsableResuelto);
        return this;
    }

    public void setResponsableResuelto(String responsableResuelto) {
        this.responsableResuelto = responsableResuelto;
    }

    public NivelGuardia getNivelResuelto() {
        return this.nivelResuelto;
    }

    public IncidenteDeProveedor nivelResuelto(NivelGuardia nivelResuelto) {
        this.setNivelResuelto(nivelResuelto);
        return this;
    }

    public void setNivelResuelto(NivelGuardia nivelResuelto) {
        this.nivelResuelto = nivelResuelto;
    }

    public Boolean getHuboCobertura() {
        return this.huboCobertura;
    }

    public IncidenteDeProveedor huboCobertura(Boolean huboCobertura) {
        this.setHuboCobertura(huboCobertura);
        return this;
    }

    public void setHuboCobertura(Boolean huboCobertura) {
        this.huboCobertura = huboCobertura;
    }

    public String getNotas() {
        return this.notas;
    }

    public IncidenteDeProveedor notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Incidente getIncidente() {
        return this.incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public IncidenteDeProveedor incidente(Incidente incidente) {
        this.setIncidente(incidente);
        return this;
    }

    public ServicioDeProveedor getServicioDeProveedor() {
        return this.servicioDeProveedor;
    }

    public void setServicioDeProveedor(ServicioDeProveedor servicioDeProveedor) {
        this.servicioDeProveedor = servicioDeProveedor;
    }

    public IncidenteDeProveedor servicioDeProveedor(ServicioDeProveedor servicioDeProveedor) {
        this.setServicioDeProveedor(servicioDeProveedor);
        return this;
    }

    public User getAbiertoPor() {
        return this.abiertoPor;
    }

    public void setAbiertoPor(User user) {
        this.abiertoPor = user;
    }

    public IncidenteDeProveedor abiertoPor(User user) {
        this.setAbiertoPor(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncidenteDeProveedor)) {
            return false;
        }
        return getId() != null && getId().equals(((IncidenteDeProveedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncidenteDeProveedor{" +
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
            "}";
    }
}
