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
 * Persona del lado del proveedor que puede estar de guardia.
 */
@Entity
@Table(name = "contacto_de_proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactoDeProveedor implements Serializable {

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

    @NotNull
    @Size(max = 120)
    @Column(name = "email", length = 120, nullable = false)
    private String email;

    @Size(max = 40)
    @Column(name = "telefono", length = 40)
    private String telefono;

    @Size(max = 60)
    @Column(name = "rol", length = 60)
    private String rol;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "servicios", "contactos" }, allowSetters = true)
    private Proveedor proveedor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contacto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacto" }, allowSetters = true)
    private Set<TurnoDeProveedor> turnos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactoDeProveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ContactoDeProveedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactoDeProveedor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public ContactoDeProveedor telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return this.rol;
    }

    public ContactoDeProveedor rol(String rol) {
        this.setRol(rol);
        return this;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public ContactoDeProveedor activo(Boolean activo) {
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

    public ContactoDeProveedor proveedor(Proveedor proveedor) {
        this.setProveedor(proveedor);
        return this;
    }

    public Set<TurnoDeProveedor> getTurnos() {
        return this.turnos;
    }

    public void setTurnos(Set<TurnoDeProveedor> turnoDeProveedors) {
        if (this.turnos != null) {
            this.turnos.forEach(i -> i.setContacto(null));
        }
        if (turnoDeProveedors != null) {
            turnoDeProveedors.forEach(i -> i.setContacto(this));
        }
        this.turnos = turnoDeProveedors;
    }

    public ContactoDeProveedor turnos(Set<TurnoDeProveedor> turnoDeProveedors) {
        this.setTurnos(turnoDeProveedors);
        return this;
    }

    public ContactoDeProveedor addTurno(TurnoDeProveedor turnoDeProveedor) {
        this.turnos.add(turnoDeProveedor);
        turnoDeProveedor.setContacto(this);
        return this;
    }

    public ContactoDeProveedor removeTurno(TurnoDeProveedor turnoDeProveedor) {
        this.turnos.remove(turnoDeProveedor);
        turnoDeProveedor.setContacto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactoDeProveedor)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactoDeProveedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactoDeProveedor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", rol='" + getRol() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
