package ar.edu.um.isa.oncall.domain;

import ar.edu.um.isa.oncall.domain.enumeration.CoberturaProveedor;
import ar.edu.um.isa.oncall.domain.enumeration.TipoProveedor;
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
 * Un tercero del que dependemos: nube, pasarela de pagos, correo.
 * La zona horaria y la cobertura son lo que permite resolver quien
 * esta de guardia del lado de ellos en un instante dado.
 */
@Entity
@Table(name = "proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proveedor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoProveedor tipo;

    @NotNull
    @Size(max = 60)
    @Column(name = "zona_horaria", length = 60, nullable = false)
    private String zonaHoraria;

    @Size(max = 40)
    @Column(name = "telefono_contacto", length = 40)
    private String telefonoContacto;

    @Size(max = 120)
    @Column(name = "email_contacto", length = 120)
    private String emailContacto;

    @Size(max = 500)
    @Column(name = "url_soporte", length = 500)
    private String urlSoporte;

    @Size(max = 500)
    @Column(name = "url_estado", length = 500)
    private String urlEstado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cobertura", nullable = false)
    private CoberturaProveedor cobertura;

    @Min(value = 1)
    @Max(value = 10080)
    @Column(name = "sla_respuesta_minutos")
    private Integer slaRespuestaMinutos;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proveedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proveedor", "servicioInternos", "tickets" }, allowSetters = true)
    private Set<ServicioDeProveedor> servicios = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proveedor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proveedor", "turnos" }, allowSetters = true)
    private Set<ContactoDeProveedor> contactos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proveedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoProveedor getTipo() {
        return this.tipo;
    }

    public Proveedor tipo(TipoProveedor tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoProveedor tipo) {
        this.tipo = tipo;
    }

    public String getZonaHoraria() {
        return this.zonaHoraria;
    }

    public Proveedor zonaHoraria(String zonaHoraria) {
        this.setZonaHoraria(zonaHoraria);
        return this;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public String getTelefonoContacto() {
        return this.telefonoContacto;
    }

    public Proveedor telefonoContacto(String telefonoContacto) {
        this.setTelefonoContacto(telefonoContacto);
        return this;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getEmailContacto() {
        return this.emailContacto;
    }

    public Proveedor emailContacto(String emailContacto) {
        this.setEmailContacto(emailContacto);
        return this;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public String getUrlSoporte() {
        return this.urlSoporte;
    }

    public Proveedor urlSoporte(String urlSoporte) {
        this.setUrlSoporte(urlSoporte);
        return this;
    }

    public void setUrlSoporte(String urlSoporte) {
        this.urlSoporte = urlSoporte;
    }

    public String getUrlEstado() {
        return this.urlEstado;
    }

    public Proveedor urlEstado(String urlEstado) {
        this.setUrlEstado(urlEstado);
        return this;
    }

    public void setUrlEstado(String urlEstado) {
        this.urlEstado = urlEstado;
    }

    public CoberturaProveedor getCobertura() {
        return this.cobertura;
    }

    public Proveedor cobertura(CoberturaProveedor cobertura) {
        this.setCobertura(cobertura);
        return this;
    }

    public void setCobertura(CoberturaProveedor cobertura) {
        this.cobertura = cobertura;
    }

    public Integer getSlaRespuestaMinutos() {
        return this.slaRespuestaMinutos;
    }

    public Proveedor slaRespuestaMinutos(Integer slaRespuestaMinutos) {
        this.setSlaRespuestaMinutos(slaRespuestaMinutos);
        return this;
    }

    public void setSlaRespuestaMinutos(Integer slaRespuestaMinutos) {
        this.slaRespuestaMinutos = slaRespuestaMinutos;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Proveedor activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<ServicioDeProveedor> getServicios() {
        return this.servicios;
    }

    public void setServicios(Set<ServicioDeProveedor> servicioDeProveedors) {
        if (this.servicios != null) {
            this.servicios.forEach(i -> i.setProveedor(null));
        }
        if (servicioDeProveedors != null) {
            servicioDeProveedors.forEach(i -> i.setProveedor(this));
        }
        this.servicios = servicioDeProveedors;
    }

    public Proveedor servicios(Set<ServicioDeProveedor> servicioDeProveedors) {
        this.setServicios(servicioDeProveedors);
        return this;
    }

    public Proveedor addServicio(ServicioDeProveedor servicioDeProveedor) {
        this.servicios.add(servicioDeProveedor);
        servicioDeProveedor.setProveedor(this);
        return this;
    }

    public Proveedor removeServicio(ServicioDeProveedor servicioDeProveedor) {
        this.servicios.remove(servicioDeProveedor);
        servicioDeProveedor.setProveedor(null);
        return this;
    }

    public Set<ContactoDeProveedor> getContactos() {
        return this.contactos;
    }

    public void setContactos(Set<ContactoDeProveedor> contactoDeProveedors) {
        if (this.contactos != null) {
            this.contactos.forEach(i -> i.setProveedor(null));
        }
        if (contactoDeProveedors != null) {
            contactoDeProveedors.forEach(i -> i.setProveedor(this));
        }
        this.contactos = contactoDeProveedors;
    }

    public Proveedor contactos(Set<ContactoDeProveedor> contactoDeProveedors) {
        this.setContactos(contactoDeProveedors);
        return this;
    }

    public Proveedor addContacto(ContactoDeProveedor contactoDeProveedor) {
        this.contactos.add(contactoDeProveedor);
        contactoDeProveedor.setProveedor(this);
        return this;
    }

    public Proveedor removeContacto(ContactoDeProveedor contactoDeProveedor) {
        this.contactos.remove(contactoDeProveedor);
        contactoDeProveedor.setProveedor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proveedor)) {
            return false;
        }
        return getId() != null && getId().equals(((Proveedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proveedor{" +
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
