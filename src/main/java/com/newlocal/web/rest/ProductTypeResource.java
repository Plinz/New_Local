package com.newlocal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.newlocal.domain.ProductType;
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
import com.newlocal.domain.ProductType;
import com.newlocal.service.ProductTypeQueryService;
import com.newlocal.service.ProductTypeService;
import com.newlocal.service.dto.ProductTypeCriteria;
import com.newlocal.service.dto.ProductTypeDTO;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ProductType.
 */
@RestController
@RequestMapping("/api")
public class ProductTypeResource {

    private final Logger log = LoggerFactory.getLogger(ProductTypeResource.class);

    private static final String ENTITY_NAME = "productType";

    private ProductTypeService productTypeService;

    private ProductTypeQueryService productTypeQueryService;

    public ProductTypeResource(ProductTypeService productTypeService, ProductTypeQueryService productTypeQueryService) {
        this.productTypeService = productTypeService;
        this.productTypeQueryService = productTypeQueryService;
    }

    /**
     * POST  /product-types : Create a new productType.
     *
     * @param productTypeDTO the productTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productTypeDTO, or with status 400 (Bad Request) if the productType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-types")
    @Timed
    public ResponseEntity<ProductTypeDTO> createProductType(@Valid @RequestBody ProductTypeDTO productTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ProductType : {}", productTypeDTO);
        if (productTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new productType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTypeDTO result = productTypeService.save(productTypeDTO);
        return ResponseEntity.created(new URI("/api/product-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-types : Updates an existing productType.
     *
     * @param productTypeDTO the productTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productTypeDTO,
     * or with status 400 (Bad Request) if the productTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the productTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-types")
    @Timed
    public ResponseEntity<ProductTypeDTO> updateProductType(@Valid @RequestBody ProductTypeDTO productTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ProductType : {}", productTypeDTO);
        if (productTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductTypeDTO result = productTypeService.save(productTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-types : get all the productTypes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of productTypes in body
     */
    @GetMapping("/product-types")
    @Timed
    public ResponseEntity<List<ProductTypeDTO>> getAllProductTypes(ProductTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductTypes by criteria: {}", criteria);
        Page<ProductTypeDTO> page = productTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /product-types/count : count all the productTypes.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/product-types/count")
    @Timed
    public ResponseEntity<Long> countProductTypes(ProductTypeCriteria criteria) {
        log.debug("REST request to count ProductTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productTypeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /product-types/:id : get the "id" productType.
     *
     * @param id the id of the productTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-types/{id}")
    @Timed
    public ResponseEntity<ProductTypeDTO> getProductType(@PathVariable Long id) {
        log.debug("REST request to get ProductType : {}", id);
        Optional<ProductTypeDTO> productTypeDTO = productTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productTypeDTO);
    }

    /**
     * GET /product-types/currentUser : get the current user product-types
     *
     * @return the ResponseEntity with status 200 (OK) and with body the product-types, or with status 404 (Not Found)
     */
    @GetMapping("/product-types/currentUser")
    @Timed
    public ResponseEntity<List<ProductTypeDTO>> getProductTypesByCurrentUser(){
        log.debug("REST request to get ProductTypes of the current user : {}");
        List<ProductTypeDTO> productTypes = productTypeService.findByCurrentUser();
        return ResponseEntity.ok().body(productTypes);
    }

    /**
     * DELETE  /product-types/:id : delete the "id" productType.
     *
     * @param id the id of the productTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        log.debug("REST request to delete ProductType : {}", id);
        productTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-types?query=:query : search for the productType corresponding
     * to the query.
     *
     * @param query the query of the productType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/product-types")
    @Timed
    public ResponseEntity<List<ProductTypeDTO>> searchProductTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductTypes for query {}", query);
        Page<ProductTypeDTO> page = productTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/product-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
