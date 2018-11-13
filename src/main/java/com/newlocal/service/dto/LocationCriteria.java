package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Location entity. This class is used in LocationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /locations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter city;

    private StringFilter country;

    private StringFilter zip;

    private StringFilter address;

    private DoubleFilter lon;

    private DoubleFilter lat;

    private LongFilter userId;

    public LocationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getZip() {
        return zip;
    }

    public void setZip(StringFilter zip) {
        this.zip = zip;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public DoubleFilter getLon() {
        return lon;
    }

    public void setLon(DoubleFilter lon) {
        this.lon = lon;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationCriteria that = (LocationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(city, that.city) &&
            Objects.equals(country, that.country) &&
            Objects.equals(zip, that.zip) &&
            Objects.equals(address, that.address) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        city,
        country,
        zip,
        address,
        lon,
        lat,
        userId
        );
    }

    @Override
    public String toString() {
        return "LocationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (zip != null ? "zip=" + zip + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (lon != null ? "lon=" + lon + ", " : "") +
                (lat != null ? "lat=" + lat + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
