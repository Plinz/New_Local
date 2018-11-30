package com.newlocal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Holding.
 */
@Entity
@Table(name = "holding")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "holding")
public class Holding implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 14, max = 14)
    @Column(name = "siret", length = 14, nullable = false)
    private String siret;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @OneToOne    @JoinColumn(unique = true)
    private Image image;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiret() {
        return siret;
    }

    public Holding siret(String siret) {
        this.siret = siret;
        return this;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getName() {
        return name;
    }

    public Holding name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Holding description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public Holding image(Image image) {
        this.image = image;
        return this;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public Holding location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getOwner() {
        return owner;
    }

    public Holding owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Holding holding = (Holding) o;
        if (holding.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), holding.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Holding{" +
            "id=" + getId() +
            ", siret='" + getSiret() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
