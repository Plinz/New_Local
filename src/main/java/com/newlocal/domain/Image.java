package com.newlocal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @ManyToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Stock> stocks = new HashSet<>();

    @ManyToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ProductType> productTypes = new HashSet<>();

    @ManyToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Holding> holdings = new HashSet<>();

    @ManyToMany(mappedBy = "images")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Warehouse> warehouses = new HashSet<>();

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

    public Image name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Image description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public Image image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Image imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public Image stocks(Set<Stock> stocks) {
        this.stocks = stocks;
        return this;
    }

    public Image addStock(Stock stock) {
        this.stocks.add(stock);
        stock.getImages().add(this);
        return this;
    }

    public Image removeStock(Stock stock) {
        this.stocks.remove(stock);
        stock.getImages().remove(this);
        return this;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Image categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Image addCategory(Category category) {
        this.categories.add(category);
        category.getImages().add(this);
        return this;
    }

    public Image removeCategory(Category category) {
        this.categories.remove(category);
        category.getImages().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<ProductType> getProductTypes() {
        return productTypes;
    }

    public Image productTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
        return this;
    }

    public Image addProductType(ProductType productType) {
        this.productTypes.add(productType);
        productType.getImages().add(this);
        return this;
    }

    public Image removeProductType(ProductType productType) {
        this.productTypes.remove(productType);
        productType.getImages().remove(this);
        return this;
    }

    public void setProductTypes(Set<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public Set<Holding> getHoldings() {
        return holdings;
    }

    public Image holdings(Set<Holding> holdings) {
        this.holdings = holdings;
        return this;
    }

    public Image addHolding(Holding holding) {
        this.holdings.add(holding);
        holding.getImages().add(this);
        return this;
    }

    public Image removeHolding(Holding holding) {
        this.holdings.remove(holding);
        holding.getImages().remove(this);
        return this;
    }

    public void setHoldings(Set<Holding> holdings) {
        this.holdings = holdings;
    }

    public Set<Warehouse> getWarehouses() {
        return warehouses;
    }

    public Image warehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
        return this;
    }

    public Image addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
        warehouse.getImages().add(this);
        return this;
    }

    public Image removeWarehouse(Warehouse warehouse) {
        this.warehouses.remove(warehouse);
        warehouse.getImages().remove(this);
        return this;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
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
        Image image = (Image) o;
        if (image.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), image.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
