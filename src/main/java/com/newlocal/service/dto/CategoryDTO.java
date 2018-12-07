package com.newlocal.service.dto;

import javax.validation.constraints.*;

import com.newlocal.domain.Category;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Category entity.
 */
public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 4979061060834672726L;

	private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 300)
    private String description;

    private Long imageId;

    private Long categoryParentId;
    
    private ImageDTO image;
    
    public CategoryDTO(){
    	
    }

    public CategoryDTO(Category category){
    	this.id = category.getId();
    	this.name = category.getName();
    	this.description = category.getDescription();
    	if(category.getImage() != null){
    		this.imageId = category.getImage().getId();
    		this.image = new ImageDTO(category.getImage());   	
    	}
    	if(category.getCategoryParent() != null){
    		this.categoryParentId = category.getCategoryParent().getId();
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

    public Long getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Long categoryId) {
        this.categoryParentId = categoryId;
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

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (categoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image=" + getImageId() +
            ", categoryParent=" + getCategoryParentId() +
            "}";
    }
}
