package com.newlocal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity_init")
    private Integer quantityInit;

    @Column(name = "quantity_remaining")
    private Integer quantityRemaining;

    @Column(name = "price_unit")
    private Double priceUnit;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "on_sale_date")
    private Instant onSaleDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "bio")
    private Boolean bio;

    @Column(name = "available")
    private Boolean available;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ProductType productType;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Holding holding;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User seller;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Stock name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Stock description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityInit() {
        return quantityInit;
    }

    public Stock quantityInit(Integer quantityInit) {
        this.quantityInit = quantityInit;
        return this;
    }

    public void setQuantityInit(Integer quantityInit) {
        this.quantityInit = quantityInit;
    }

    public Integer getQuantityRemaining() {
        return quantityRemaining;
    }

    public Stock quantityRemaining(Integer quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
        return this;
    }

    public void setQuantityRemaining(Integer quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public Stock priceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
        return this;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public byte[] getImage() {
        return image;
    }

    public Stock image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Stock imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Instant getOnSaleDate() {
        return onSaleDate;
    }

    public Stock onSaleDate(Instant onSaleDate) {
        this.onSaleDate = onSaleDate;
        return this;
    }

    public void setOnSaleDate(Instant onSaleDate) {
        this.onSaleDate = onSaleDate;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public Stock expiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean isBio() {
        return bio;
    }

    public Stock bio(Boolean bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(Boolean bio) {
        this.bio = bio;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Stock available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Stock productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Holding getHolding() {
        return holding;
    }

    public Stock holding(Holding holding) {
        this.holding = holding;
        return this;
    }

    public void setHolding(Holding holding) {
        this.holding = holding;
    }

    public User getSeller() {
        return seller;
    }

    public Stock seller(User user) {
        this.seller = user;
        return this;
    }

    public void setSeller(User user) {
        this.seller = user;
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
        Stock stock = (Stock) o;
        if (stock.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantityInit=" + getQuantityInit() +
            ", quantityRemaining=" + getQuantityRemaining() +
            ", priceUnit=" + getPriceUnit() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", onSaleDate='" + getOnSaleDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", bio='" + isBio() + "'" +
            ", available='" + isAvailable() + "'" +
            "}";
    }
}
