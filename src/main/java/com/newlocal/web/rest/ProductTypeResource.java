package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.ProductType;
import com.newlocal.service.ProductTypeService;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.service.dto.ProductTypeCriteria;
import com.newlocal.service.ProductTypeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
     * @param productType the productType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productType, or with status 400 (Bad Request) if the productType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-types")
    @Timed
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductType productType) throws URISyntaxException {
        log.debug("REST request to save ProductType : {}", productType);
        if (productType.getId() != null) {
            throw new BadRequestAlertException("A new productType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductType result = productTypeService.save(productType);
        return ResponseEntity.created(new URI("/api/product-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-types : Updates an existing productType.
     *
     * @param productType the productType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productType,
     * or with status 400 (Bad Request) if the productType is not valid,
     * or with status 500 (Internal Server Error) if the productType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-types")
    @Timed
    public ResponseEntity<ProductType> updateProductType(@RequestBody ProductType productType) throws URISyntaxException {
        log.debug("REST request to update ProductType : {}", productType);
        if (productType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductType result = productTypeService.save(productType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-types : get all the productTypes.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of productTypes in body
     */
    @GetMapping("/product-types")
    @Timed
    public ResponseEntity<List<ProductType>> getAllProductTypes(ProductTypeCriteria criteria) {
        log.debug("REST request to get ProductTypes by criteria: {}", criteria);
        List<ProductType> entityList = productTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
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
     * @param id the id of the productType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productType, or with status 404 (Not Found)
     */
    @GetMapping("/product-types/{id}")
    @Timed
    public ResponseEntity<ProductType> getProductType(@PathVariable Long id) {
        log.debug("REST request to get ProductType : {}", id);
        Optional<ProductType> productType = productTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productType);
    }

    /**
     * DELETE  /product-types/:id : delete the "id" productType.
     *
     * @param id the id of the productType to delete
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
     * @return the result of the search
     */
    @GetMapping("/_search/product-types")
    @Timed
    public List<ProductType> searchProductTypes(@RequestParam String query) {
        log.debug("REST request to search ProductTypes for query {}", query);
        return productTypeService.search(query);
    }

}
