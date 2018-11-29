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
 * A Grade.
 */
@Entity
@Table(name = "grade")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "grade")
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "grade", nullable = false)
    private Double grade;

    @NotNull
    @Min(value = 0)
    @Column(name = "nb_voter", nullable = false)
    private Integer nbVoter;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User seller;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private ProductType productType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrade() {
        return grade;
    }

    public Grade grade(Double grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getNbVoter() {
        return nbVoter;
    }

    public Grade nbVoter(Integer nbVoter) {
        this.nbVoter = nbVoter;
        return this;
    }

    public void setNbVoter(Integer nbVoter) {
        this.nbVoter = nbVoter;
    }

    public User getSeller() {
        return seller;
    }

    public Grade seller(User user) {
        this.seller = user;
        return this;
    }

    public void setSeller(User user) {
        this.seller = user;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Grade productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
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
        Grade grade = (Grade) o;
        if (grade.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), grade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Grade{" +
            "id=" + getId() +
            ", grade=" + getGrade() +
            ", nbVoter=" + getNbVoter() +
            "}";
    }
}
