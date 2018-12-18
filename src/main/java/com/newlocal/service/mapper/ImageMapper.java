package com.newlocal.service.mapper;

import org.mapstruct.Mapper;

import com.newlocal.domain.Image;
import com.newlocal.service.dto.ImageDTO;

/**
 * Mapper for the entity Image and its DTO ImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {

    ImageDTO toDto(Image location);

    Image toEntity(ImageDTO locationDTO);


    default Image fromId(Long id) {
        if (id == null) {
            return null;
        }
        Image image = new Image();
        image.setId(id);
        return image;
    }
}
