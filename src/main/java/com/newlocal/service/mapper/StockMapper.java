package com.newlocal.service.mapper;

import com.newlocal.domain.*;
import com.newlocal.service.dto.StockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Stock and its DTO StockDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, ProductTypeMapper.class, HoldingMapper.class, UserMapper.class, WarehouseMapper.class})
public interface StockMapper extends EntityMapper<StockDTO, Stock> {

    @Mapping(source = "image.id", target = "imageId")
    @Mapping(source = "productType.id", target = "productTypeId")
    @Mapping(source = "holding.id", target = "holdingId")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    StockDTO toDto(Stock stock);

    @Mapping(source = "imageId", target = "image")
    @Mapping(source = "productTypeId", target = "productType")
    @Mapping(source = "holdingId", target = "holding")
    @Mapping(source = "sellerId", target = "seller")
    @Mapping(source = "warehouseId", target = "warehouse")
    Stock toEntity(StockDTO stockDTO);

    default Stock fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stock stock = new Stock();
        stock.setId(id);
        return stock;
    }
}
