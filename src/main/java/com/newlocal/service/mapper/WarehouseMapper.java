package com.newlocal.service.mapper;

import com.newlocal.domain.*;
import com.newlocal.service.dto.WarehouseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Warehouse and its DTO WarehouseDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, LocationMapper.class})
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {

    @Mapping(source = "image.id", target = "imageId")
    @Mapping(source = "location.id", target = "locationId")
    WarehouseDTO toDto(Warehouse warehouse);

    @Mapping(source = "imageId", target = "image")
    @Mapping(source = "locationId", target = "location")
    Warehouse toEntity(WarehouseDTO warehouseDTO);

    default Warehouse fromId(Long id) {
        if (id == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        return warehouse;
    }
}
