package com.newlocal.web.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.service.ImageQueryService;
import com.newlocal.service.ImageService;
import com.newlocal.service.dto.ImageCriteria;
import com.newlocal.service.dto.ImageDTO;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Image.
 */
@RestController
@RequestMapping("/api")
public class ImageResource {

    private final Logger log = LoggerFactory.getLogger(ImageResource.class);

    private static final String ENTITY_NAME = "image";

    private ImageService imageService;

    private ImageQueryService imageQueryService;

    public ImageResource(ImageService imageService, ImageQueryService imageQueryService) {
        this.imageService = imageService;
        this.imageQueryService = imageQueryService;
    }

    /**
     * POST  /images : Create a new image.
     *
     * @param image the image to create
     * @return the ResponseEntity with status 201 (Created) and with body the new image, or with status 400 (Bad Request) if the image has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/images")
    @Timed
    public ResponseEntity<ImageDTO> createImage(@Valid @RequestBody ImageDTO image) throws URISyntaxException {
        log.debug("REST request to save Image : {}", image);
        if (image.getId() != null) {
            throw new BadRequestAlertException("A new image cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (image.getImage() != null){
	        try {
	        	Random r = new Random();
	        	File imageFile = new File("src/main/resources/images/"+image.getName());
		        while(imageFile.exists() || imageFile.isDirectory()){
		        	imageFile = new File("src/main/resources/images/"+image.getName()+"_"+r.nextInt(999999999));
		        }
				FileUtils.writeByteArrayToFile(imageFile, image.getImage());
				image.setImagePath(imageFile.getPath());
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
        }
        log.debug("Fin REST request to save Image : {}", image);
        ImageDTO result = new ImageDTO(imageService.save(image));
        return ResponseEntity.created(new URI("/api/images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /images : Updates an existing image.
     *
     * @param image the image to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated image,
     * or with status 400 (Bad Request) if the image is not valid,
     * or with status 500 (Internal Server Error) if the image couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/images")
    @Timed
    public ResponseEntity<ImageDTO> updateImage(@Valid @RequestBody ImageDTO image) throws URISyntaxException {
        log.debug("REST request to update Image : {}", image);
        if (image.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (image.getImage() != null){
	        try {
	        	Random r = new Random();
	        	File imageFile = new File("src/main/resources/images/"+image.getName());
		        while(imageFile.exists() || imageFile.isDirectory()){
		        	imageFile = new File("src/main/resources/images/"+image.getName()+"_"+r.nextInt(999999999));
		        }
				FileUtils.writeByteArrayToFile(imageFile, image.getImage());
				image.setImagePath(imageFile.getPath());
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
        }
        ImageDTO result = new ImageDTO(imageService.save(image));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, image.getId().toString()))
            .body(result);
    }

    /**
     * GET  /images : get all the images.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of images in body
     */
    @GetMapping("/images")
    @Timed
    public ResponseEntity<List<ImageDTO>> getAllImages(ImageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Images by criteria: {}", criteria);
        Page<ImageDTO> page = imageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /images/count : count all the images.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/images/count")
    @Timed
    public ResponseEntity<Long> countImages(ImageCriteria criteria) {
        log.debug("REST request to count Images by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /images/:id : get the "id" image.
     *
     * @param id the id of the image to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the image, or with status 404 (Not Found)
     */
    @GetMapping("/images/{id}")
    @Timed
    public ResponseEntity<ImageDTO> getImage(@PathVariable Long id) {
        log.debug("REST request to get Image : {}", id);
        Optional<ImageDTO> image = imageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(image);
    }

    /**
     * DELETE  /images/:id : delete the "id" image.
     *
     * @param id the id of the image to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/images/{id}")
    @Timed
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        log.debug("REST request to delete Image : {}", id);
        Optional<ImageDTO> image = imageService.findOne(id);
        if (image.isPresent()) {
        	File imageFile = new File(image.get().getImagePath());
        	if (imageFile.exists() && imageFile.isFile()) {
        		imageFile.delete();
        	}
        }
        imageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/images?query=:query : search for the image corresponding
     * to the query.
     *
     * @param query the query of the image search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/images")
    @Timed
    public ResponseEntity<List<ImageDTO>> searchImages(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Images for query {}", query);
        Page<ImageDTO> page = imageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
