package ar.edu.um.isa.oncall.domain;

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
 * La guardia del proveedor. Entidad propia y no una Rotacion, porque
 * una rotacion pertenece siempre a un Equipo y esta no tiene ninguno.
 */
@Entity
@Table(name = "turno_de_proveedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurnoDeProveedor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "desde", nullable = false)
    private Instant desde;

    @NotNull
    @Column(name = "hasta", nullable = false)
    private Instant hasta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private NivelGuardia nivel;

    @NotNull
    @Column(name = "es_reemplazo", nullable = false)
    private Boolean esReemplazo;

    @Size(max = 500)
    @Column(name = "nota", length = 500)
    private String nota;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "proveedor", "turnos" }, allowSetters = true)
    private ContactoDeProveedor contacto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TurnoDeProveedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDesde() {
        return this.desde;
    }

    public TurnoDeProveedor desde(Instant desde) {
        this.setDesde(desde);
        return this;
    }

    public void setDesde(Instant desde) {
        this.desde = desde;
    }

    public Instant getHasta() {
        return this.hasta;
    }

    public TurnoDeProveedor hasta(Instant hasta) {
        this.setHasta(hasta);
        return this;
    }

    public void setHasta(Instant hasta) {
        this.hasta = hasta;
    }

    public NivelGuardia getNivel() {
        return this.nivel;
    }

    public TurnoDeProveedor nivel(NivelGuardia nivel) {
        this.setNivel(nivel);
        return this;
    }

    public void setNivel(NivelGuardia nivel) {
        this.nivel = nivel;
    }

    public Boolean getEsReemplazo() {
        return this.esReemplazo;
    }

    public TurnoDeProveedor esReemplazo(Boolean esReemplazo) {
        this.setEsReemplazo(esReemplazo);
        return this;
    }

    public void setEsReemplazo(Boolean esReemplazo) {
        this.esReemplazo = esReemplazo;
    }

    public String getNota() {
        return this.nota;
    }

    public TurnoDeProveedor nota(String nota) {
        this.setNota(nota);
        return this;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public ContactoDeProveedor getContacto() {
        return this.contacto;
    }

    public void setContacto(ContactoDeProveedor contactoDeProveedor) {
        this.contacto = contactoDeProveedor;
    }

    public TurnoDeProveedor contacto(ContactoDeProveedor contactoDeProveedor) {
        this.setContacto(contactoDeProveedor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurnoDeProveedor)) {
            return false;
        }
        return getId() != null && getId().equals(((TurnoDeProveedor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnoDeProveedor{" +
            "id=" + getId() +
            ", desde='" + getDesde() + "'" +
            ", hasta='" + getHasta() + "'" +
            ", nivel='" + getNivel() + "'" +
            ", esReemplazo='" + getEsReemplazo() + "'" +
            ", nota='" + getNota() + "'" +
            "}";
    }
}
