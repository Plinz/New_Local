package com.newlocal.service.mapper;

import com.newlocal.domain.*;
import com.newlocal.service.dto.HoldingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Holding and its DTO HoldingDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, LocationMapper.class, UserMapper.class})
public interface HoldingMapper extends EntityMapper<HoldingDTO, Holding> {

    @Mapping(source = "image.id", target = "imageId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "owner.id", target = "ownerId")
    HoldingDTO toDto(Holding holding);

    @Mapping(source = "imageId", target = "image")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "ownerId", target = "owner")
    Holding toEntity(HoldingDTO holdingDTO);

    default Holding fromId(Long id) {
        if (id == null) {
            return null;
        }
        Holding holding = new Holding();
        holding.setId(id);
        return holding;
    }
}
