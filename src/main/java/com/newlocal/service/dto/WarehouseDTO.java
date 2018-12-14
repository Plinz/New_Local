package com.newlocal.service.dto;

import javax.validation.constraints.*;

import com.newlocal.domain.Warehouse;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Warehouse entity.
 */
public class WarehouseDTO implements Serializable {

	private static final long serialVersionUID = -2554651063013009165L;

	private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 300)
    private String description;

    @NotNull
    @Size(min = 4, max = 15)
    private String tel;

    private Long imageId;

    private Long locationId;
    
    private ImageDTO image;
    
    private LocationDTO location;
    
    public WarehouseDTO(){
    	
    }
    
    public WarehouseDTO(Warehouse warehouse){
    	this.id = warehouse.getId();
    	this.name = warehouse.getName();
    	this.description = warehouse.getDescription();
    	this.tel = warehouse.getTel();
    	if(warehouse.getImage() != null){
	    	this.imageId = warehouse.getImage().getId();
	    	this.image = new ImageDTO(warehouse.getImage());
    	}
    	if(warehouse.getLocation() != null){
    		this.locationId = warehouse.getLocation().getId();
    		this.location = new LocationDTO(warehouse.getLocation());
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public ImageDTO getImage() {
		return image;
	}

	public void setImage(ImageDTO image) {
		this.image = image;
	}

    public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WarehouseDTO warehouseDTO = (WarehouseDTO) o;
        if (warehouseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), warehouseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WarehouseDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", tel='" + getTel() + "'" +
            ", image=" + getImageId() +
            ", location=" + getLocationId() +
            "}";
    }
}
