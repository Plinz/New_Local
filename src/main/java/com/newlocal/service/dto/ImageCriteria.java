package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Image entity. This class is used in ImageResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /images?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ImageCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter stockId;

    private LongFilter categoryId;

    private LongFilter productTypeId;

    private LongFilter holdingId;

    private LongFilter warehouseId;

    public ImageCriteria() {
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

    public LongFilter getStockId() {
        return stockId;
    }

    public void setStockId(LongFilter stockId) {
        this.stockId = stockId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final ImageCriteria that = (ImageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(stockId, that.stockId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(productTypeId, that.productTypeId) &&
            Objects.equals(holdingId, that.holdingId) &&
            Objects.equals(warehouseId, that.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        stockId,
        categoryId,
        productTypeId,
        holdingId,
        warehouseId
        );
    }

    @Override
    public String toString() {
        return "ImageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (stockId != null ? "stockId=" + stockId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (productTypeId != null ? "productTypeId=" + productTypeId + ", " : "") +
                (holdingId != null ? "holdingId=" + holdingId + ", " : "") +
                (warehouseId != null ? "warehouseId=" + warehouseId + ", " : "") +
            "}";
    }

}
