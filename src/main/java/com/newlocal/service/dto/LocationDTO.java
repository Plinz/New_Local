package com.newlocal.service.dto;

import javax.validation.constraints.*;

import com.newlocal.domain.Location;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Location entity.
 */
public class LocationDTO implements Serializable {

	private static final long serialVersionUID = 7568896360393816420L;

	private Long id;

    @Size(min = 1, max = 100)
    private String city;

    @Size(min = 1, max = 100)
    private String country;

    @NotNull
    @Size(min = 1, max = 30)
    private String zip;

    @Size(min = 1, max = 200)
    private String address;

    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    private Double lon;

    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    private Double lat;

    private Long userId;
    
    private UserDTO user;
    
    public LocationDTO(){
    	
    }

    public LocationDTO(Location location) {
		this.id = location.getId();
		this.city = location.getCity();
		this.country = location.getCountry();
		this.zip = location.getZip();
		this.address = location.getAddress();
		this.lon = location.getLon();
		this.lat = location.getLat();
		if(location.getUser() != null){
			this.userId = location.getUser().getId();
			this.user = new UserDTO(location.getUser());
		}
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (locationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", zip='" + getZip() + "'" +
            ", address='" + getAddress() + "'" +
            ", lon=" + getLon() +
            ", lat=" + getLat() +
            ", user=" + getUserId() +
            "}";
    }
}
