package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.newlocal.domain.Holding;

/**
 * A DTO for the Holding entity.
 */
public class HoldingDTO implements Serializable {

	private static final long serialVersionUID = -6721350314311385834L;

	private Long id;

    @NotNull
    @Size(min = 14, max = 14)
    private String siret;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 300)
    private String description;

    private Long imageId;

    private Long locationId;

    private Long ownerId;
    
    private ImageDTO image;
    
    private LocationDTO location;
    
    private UserDTO owner;
    
    public HoldingDTO(){
        this.siret = "00000000000000";
        this.name = "null";
    }
    
    public HoldingDTO(Holding holding){
    	this.id = holding.getId();
    	this.siret = holding.getSiret();
    	this.name = holding.getName();
    	this.description = holding.getDescription();
    	if(holding.getImage() != null){
    		this.imageId = holding.getImage().getId();
    		this.image = new ImageDTO(holding.getImage());
    	}
    	if(holding.getLocation() != null){
    		this.locationId = holding.getLocation().getId();
    		this.location = new LocationDTO(holding.getLocation());
    	}
    	if(holding.getOwner() != null){
    		this.ownerId = holding.getOwner().getId();
    		this.owner = new UserDTO(holding.getOwner());
    	}
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public void setImage(ImageDTO image) {
		this.image = image;
	}

	public ImageDTO getImage() {
		return image;
	}

	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HoldingDTO holdingDTO = (HoldingDTO) o;
        if (holdingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), holdingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HoldingDTO{" +
            "id=" + getId() +
            ", siret='" + getSiret() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image=" + getImageId() +
            ", location=" + getLocationId() +
            ", owner=" + getOwnerId() +
            "}";
    }
}
