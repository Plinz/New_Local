package com.newlocal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Purchase.
 */
@Entity
@Table(name = "purchase")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "purchase")
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "sale_date", nullable = false)
    private Instant saleDate;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "withdraw", nullable = false)
    private Boolean withdraw;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Stock stock;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public Purchase saleDate(Instant saleDate) {
        this.saleDate = saleDate;
        return this;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Purchase quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean isWithdraw() {
        return withdraw;
    }

    public Purchase withdraw(Boolean withdraw) {
        this.withdraw = withdraw;
        return this;
    }

    public void setWithdraw(Boolean withdraw) {
        this.withdraw = withdraw;
    }

    public Stock getStock() {
        return stock;
    }

    public Purchase stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public User getClient() {
        return client;
    }

    public Purchase client(User user) {
        this.client = user;
        return this;
    }

    public void setClient(User user) {
        this.client = user;
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
        Purchase purchase = (Purchase) o;
        if (purchase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Purchase{" +
            "id=" + getId() +
            ", saleDate='" + getSaleDate() + "'" +
            ", quantity=" + getQuantity() +
            ", withdraw='" + isWithdraw() + "'" +
            "}";
    }
}
