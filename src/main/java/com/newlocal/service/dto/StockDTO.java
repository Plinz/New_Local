package com.newlocal.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;

import com.newlocal.domain.Stock;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Stock entity.
 */
public class StockDTO implements Serializable {

	private static final long serialVersionUID = 6004151691630612767L;

	private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 300)
    private String description;

    @NotNull
    @Min(value = 0)
    private Integer quantityInit;

    @NotNull
    @Min(value = 0)
    private Integer quantityRemaining;

    @NotNull
    @DecimalMin(value = "0")
    private Double priceUnit;

    @NotNull
    private Instant onSaleDate;

    @NotNull
    private Instant expiryDate;

    @NotNull
    private Boolean bio;

    @NotNull
    private Boolean available;

    private Long imageId;

    private Long productTypeId;

    private Long holdingId;

    private Long sellerId;

    private Long warehouseId;
    
    private ImageDTO image;
    
    public StockDTO(){
    	
    }
    
    public StockDTO(Stock stock){
    	this.id = stock.getId();
    	this.name = stock.getName();
    	this.description = stock.getDescription();
    	this.quantityInit = stock.getQuantityInit();
    	this.quantityRemaining = stock.getQuantityRemaining();
    	this.priceUnit = stock.getPriceUnit();
    	this.onSaleDate = stock.getOnSaleDate();
    	this.expiryDate = stock.getExpiryDate();
    	this.bio = stock.isBio();
    	this.available = stock.isAvailable();
    	if(stock.getImage() != null){
    		this.imageId = stock.getImage().getId();
    		this.image = new ImageDTO(stock.getImage());
    	}
    	if(stock.getProductType() != null){
    		this.productTypeId = stock.getProductType().getId();
    	}
    	if(stock.getHolding() != null){
    		this.holdingId = stock.getHolding().getId();
    	}
    	if(stock.getSeller() != null){
    		this.sellerId = stock.getSeller().getId();
    	}
    	if(stock.getWarehouse() != null){
    		this.warehouseId = stock.getWarehouse().getId();
    	}
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityInit() {
        return quantityInit;
    }

    public void setQuantityInit(Integer quantityInit) {
        this.quantityInit = quantityInit;
    }

    public Integer getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(Integer quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Instant getOnSaleDate() {
        return onSaleDate;
    }

    public void setOnSaleDate(Instant onSaleDate) {
        this.onSaleDate = onSaleDate;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean isBio() {
        return bio;
    }

    public void setBio(Boolean bio) {
        this.bio = bio;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(Long holdingId) {
        this.holdingId = holdingId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long userId) {
        this.sellerId = userId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public ImageDTO getImage() {
		return image;
	}

	public void setImage(ImageDTO image) {
		this.image = image;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (stockDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantityInit=" + getQuantityInit() +
            ", quantityRemaining=" + getQuantityRemaining() +
            ", priceUnit=" + getPriceUnit() +
            ", onSaleDate='" + getOnSaleDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", bio='" + isBio() + "'" +
            ", available='" + isAvailable() + "'" +
            ", image=" + getImageId() +
            ", productType=" + getProductTypeId() +
            ", holding=" + getHoldingId() +
            ", seller=" + getSellerId() +
            ", warehouse=" + getWarehouseId() +
            "}";
    }
}
