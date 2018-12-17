package com.newlocal.service.mapper;

import com.newlocal.domain.*;
import com.newlocal.service.dto.ProductTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProductType and its DTO ProductTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class, CategoryMapper.class})
public interface ProductTypeMapper extends EntityMapper<ProductTypeDTO, ProductType> {

    @Mapping(source = "image.id", target = "imageId")
    @Mapping(source = "category.id", target = "categoryId")
    ProductTypeDTO toDto(ProductType productType);

    @Mapping(source = "imageId", target = "image")
    @Mapping(source = "categoryId", target = "category")
    ProductType toEntity(ProductTypeDTO productTypeDTO);

    default ProductType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductType productType = new ProductType();
        productType.setId(id);
        return productType;
    }
}
