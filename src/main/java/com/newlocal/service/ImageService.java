package com.newlocal.service;

import com.newlocal.domain.Image;
import com.newlocal.repository.ImageRepository;
import com.newlocal.repository.search.ImageSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Image.
 */
@Service
@Transactional
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    private ImageRepository imageRepository;

    private ImageSearchRepository imageSearchRepository;

    public ImageService(ImageRepository imageRepository, ImageSearchRepository imageSearchRepository) {
        this.imageRepository = imageRepository;
        this.imageSearchRepository = imageSearchRepository;
    }

    /**
     * Save a image.
     *
     * @param image the entity to save
     * @return the persisted entity
     */
    public Image save(Image image) {
        log.debug("Request to save Image : {}", image);
        Image result = imageRepository.save(image);
        imageSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the images.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Image> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imageRepository.findAll(pageable);
    }


    /**
     * Get one image by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Image> findOne(Long id) {
        log.debug("Request to get Image : {}", id);
        return imageRepository.findById(id);
    }

    /**
     * Delete the image by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Image : {}", id);
        imageRepository.deleteById(id);
        imageSearchRepository.deleteById(id);
    }

    /**
     * Search for the image corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Image> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Images for query {}", query);
        return imageSearchRepository.search(queryStringQuery(query), pageable);    }
}
