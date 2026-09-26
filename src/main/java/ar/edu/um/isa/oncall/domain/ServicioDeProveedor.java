package ar.edu.um.isa.oncall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Componente concreto que consumimos de ese proveedor.
 * El SLA propio, si esta cargado, pisa al del proveedor.
 */
@Entity
@Table(name = "servicio_de_proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioDeProveedor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Size(max = 200)
    @Column(name = "identificador_externo", length = 200)
    private String identificadorExterno;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Min(value = 1)
    @Max(value = 10080)
    @Column(name = "sla_respuesta_minutos")
    private Integer slaRespuestaMinutos;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicios", "contactos" }, allowSetters = true)
    private Proveedor proveedor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_servicio_de_proveedor__servicio_interno",
        joinColumns = @JoinColumn(name = "servicio_de_proveedor_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_interno_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "equipo", "objetivos", "alertas", "politicas", "incidentes", "proveedorDelQueDependes" },
        allowSetters = true
    )
    private Set<Servicio> servicioInternos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicioDeProveedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "incidente", "servicioDeProveedor", "abiertoPor" }, allowSetters = true)
    private Set<IncidenteDeProveedor> tickets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServicioDeProveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ServicioDeProveedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificadorExterno() {
        return this.identificadorExterno;
    }

    public ServicioDeProveedor identificadorExterno(String identificadorExterno) {
        this.setIdentificadorExterno(identificadorExterno);
        return this;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public ServicioDeProveedor descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSlaRespuestaMinutos() {
        return this.slaRespuestaMinutos;
    }

    public ServicioDeProveedor slaRespuestaMinutos(Integer slaRespuestaMinutos) {
        this.setSlaRespuestaMinutos(slaRespuestaMinutos);
        return this;
    }

    public void setSlaRespuestaMinutos(Integer slaRespuestaMinutos) {
        this.slaRespuestaMinutos = slaRespuestaMinutos;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public ServicioDeProveedor activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Proveedor getProveedor() {
        return this.proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public ServicioDeProveedor proveedor(Proveedor proveedor) {
        this.setProveedor(proveedor);
        return this;
    }

    public Set<Servicio> getServicioInternos() {
        return this.servicioInternos;
    }

    public void setServicioInternos(Set<Servicio> servicios) {
        this.servicioInternos = servicios;
    }

    public ServicioDeProveedor servicioInternos(Set<Servicio> servicios) {
        this.setServicioInternos(servicios);
        return this;
    }

    public ServicioDeProveedor addServicioInterno(Servicio servicio) {
        this.servicioInternos.add(servicio);
        return this;
    }

    public ServicioDeProveedor removeServicioInterno(Servicio servicio) {
        this.servicioInternos.remove(servicio);
        return this;
    }

    public Set<IncidenteDeProveedor> getTickets() {
        return this.tickets;
    }

    public void setTickets(Set<IncidenteDeProveedor> incidenteDeProveedors) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.setServicioDeProveedor(null));
        }
        if (incidenteDeProveedors != null) {
            incidenteDeProveedors.forEach(i -> i.setServicioDeProveedor(this));
        }
        this.tickets = incidenteDeProveedors;
    }

    public ServicioDeProveedor tickets(Set<IncidenteDeProveedor> incidenteDeProveedors) {
        this.setTickets(incidenteDeProveedors);
        return this;
    }

    public ServicioDeProveedor addTicket(IncidenteDeProveedor incidenteDeProveedor) {
        this.tickets.add(incidenteDeProveedor);
        incidenteDeProveedor.setServicioDeProveedor(this);
        return this;
    }

    public ServicioDeProveedor removeTicket(IncidenteDeProveedor incidenteDeProveedor) {
        this.tickets.remove(incidenteDeProveedor);
        incidenteDeProveedor.setServicioDeProveedor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioDeProveedor)) {
            return false;
        }
        return getId() != null && getId().equals(((ServicioDeProveedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDeProveedor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", identificadorExterno='" + getIdentificadorExterno() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", slaRespuestaMinutos=" + getSlaRespuestaMinutos() +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
