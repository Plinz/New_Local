package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Stock entity. This class is used in StockResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stocks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter quantityInit;

    private IntegerFilter quantityRemaining;

    private DoubleFilter priceUnit;

    private InstantFilter onSaleDate;

    private InstantFilter expiryDate;

    private BooleanFilter bio;

    private BooleanFilter available;

    private LongFilter imageId;

    private LongFilter productTypeId;

    private LongFilter holdingId;

    private LongFilter sellerId;

    private LongFilter warehouseId;
    
    private StringFilter categoryName;
    
    private StringFilter holdingName;

    private StringFilter warehouseName;

    public StringFilter getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(StringFilter warehouseName) {
		this.warehouseName = warehouseName;
    }
    
    public StringFilter getHoldingName() {
		return holdingName;
	}

	public void setHoldingName(StringFilter holdingName) {
		this.holdingName = holdingName;
	}

	public StringFilter getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(StringFilter categoryName) {
		this.categoryName = categoryName;
	}

	public StockCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getQuantityInit() {
        return quantityInit;
    }

    public void setQuantityInit(IntegerFilter quantityInit) {
        this.quantityInit = quantityInit;
    }

    public IntegerFilter getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(IntegerFilter quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public DoubleFilter getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(DoubleFilter priceUnit) {
        this.priceUnit = priceUnit;
    }

    public InstantFilter getOnSaleDate() {
        return onSaleDate;
    }

    public void setOnSaleDate(InstantFilter onSaleDate) {
        this.onSaleDate = onSaleDate;
    }

    public InstantFilter getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(InstantFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BooleanFilter getBio() {
        return bio;
    }

    public void setBio(BooleanFilter bio) {
        this.bio = bio;
    }

    public BooleanFilter getAvailable() {
        return available;
    }

    public void setAvailable(BooleanFilter available) {
        this.available = available;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public LongFilter getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(LongFilter productTypeId) {
        this.productTypeId = productTypeId;
    }

    public LongFilter getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(LongFilter holdingId) {
        this.holdingId = holdingId;
    }

    public LongFilter getSellerId() {
        return sellerId;
    }

    public void setSellerId(LongFilter sellerId) {
        this.sellerId = sellerId;
    }

    public LongFilter getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(LongFilter warehouseId) {
        this.warehouseId = warehouseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockCriteria that = (StockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(quantityInit, that.quantityInit) &&
            Objects.equals(quantityRemaining, that.quantityRemaining) &&
            Objects.equals(priceUnit, that.priceUnit) &&
            Objects.equals(onSaleDate, that.onSaleDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(available, that.available) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(productTypeId, that.productTypeId) &&
            Objects.equals(holdingId, that.holdingId) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(warehouseId, that.warehouseId) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(holdingName, that.holdingName) &&
            Objects.equals(warehouseName, that.warehouseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        quantityInit,
        quantityRemaining,
        priceUnit,
        onSaleDate,
        expiryDate,
        bio,
        available,
        imageId,
        productTypeId,
        holdingId,
        sellerId,
        warehouseId,
        categoryName,
        holdingName,
        warehouseName
        );
    }

    @Override
    public String toString() {
        return "StockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (quantityInit != null ? "quantityInit=" + quantityInit + ", " : "") +
                (quantityRemaining != null ? "quantityRemaining=" + quantityRemaining + ", " : "") +
                (priceUnit != null ? "priceUnit=" + priceUnit + ", " : "") +
                (onSaleDate != null ? "onSaleDate=" + onSaleDate + ", " : "") +
                (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
                (bio != null ? "bio=" + bio + ", " : "") +
                (available != null ? "available=" + available + ", " : "") +
                (imageId != null ? "imageId=" + imageId + ", " : "") +
                (productTypeId != null ? "productTypeId=" + productTypeId + ", " : "") +
                (holdingId != null ? "holdingId=" + holdingId + ", " : "") +
                (sellerId != null ? "sellerId=" + sellerId + ", " : "") +
                (warehouseId != null ? "warehouseId=" + warehouseId + ", " : "") +
                (categoryName != null ? "categoryName=" + categoryName + ", " : "") +
                (holdingName != null ? "holdingName=" + holdingName + ", " : "") +
                (warehouseName != null ? "warehouseName=" + warehouseName + ", " : "") +
            "}";
    }

}
