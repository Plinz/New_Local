package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.newlocal.domain.ProductType;

/**
 * A DTO for the ProductType entity.
 */
public class ProductTypeDTO implements Serializable {

	private static final long serialVersionUID = -6583054045082302677L;

	private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 300)
    private String description;

    private Long imageId;

    private Long categoryId;
    
    private ImageDTO image;
    
    public ProductTypeDTO(){
    	this.name = "null";
    }
    
    public ProductTypeDTO(ProductType productType){
    	this.id = productType.getId();
    	this.name = productType.getName();
    	this.description = productType.getDescription();
    	if(productType.getImage() != null){
    		this.imageId = productType.getImage().getId();
    		this.image = new ImageDTO(productType.getImage());
    	}
    	if(productType.getCategory() != null){
    		this.categoryId = productType.getCategory().getId();
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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

        ProductTypeDTO productTypeDTO = (ProductTypeDTO) o;
        if (productTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image=" + getImageId() +
            ", category=" + getCategoryId() +
            "}";
    }
}
