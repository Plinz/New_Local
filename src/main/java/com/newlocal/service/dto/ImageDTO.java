package com.newlocal.service.dto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.newlocal.domain.Image;

/**
 * A DTO representing a Image.
 */

public class ImageDTO {
	
	public ImageDTO() {
        // Empty constructor needed for Jackson.
	}
	
	public ImageDTO(Image image){
		this.id = image.getId();
		this.name = image.getName();
		this.description = image.getDescription();
		this.imagePath = image.getImagePath();
		if (this.imagePath != null && !this.imagePath.isEmpty()){
	    	File imageFile = new File(image.getImagePath());
	    	if (imageFile != null && imageFile.exists() && imageFile.isFile()) {
	    		try {
					this.image = Files.readAllBytes(imageFile.toPath());
					this.imageContentType = Files.probeContentType(imageFile.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
		}
	}

    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 300)
    private String description;

    private String imagePath;
    
    private byte[] image;

    private String imageContentType;
    
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
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
        ImageDTO image = (ImageDTO) o;
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
		return "ImageDTO [id=" + id + ", name=" + name + ", description=" + description + ", imagePath=" + imagePath
				+ ", image=" + Arrays.toString(image) + ", imageContentType=" + imageContentType + "]";
	}

    
}
