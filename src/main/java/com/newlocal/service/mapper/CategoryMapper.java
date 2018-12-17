package com.newlocal.service.mapper;

import com.newlocal.domain.*;
import com.newlocal.service.dto.CategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

    @Mapping(source = "image.id", target = "imageId")
    @Mapping(source = "categoryParent.id", target = "categoryParentId")
    CategoryDTO toDto(Category category);

    @Mapping(source = "imageId", target = "image")
    @Mapping(source = "categoryParentId", target = "categoryParent")
    Category toEntity(CategoryDTO categoryDTO);

    default Category fromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
