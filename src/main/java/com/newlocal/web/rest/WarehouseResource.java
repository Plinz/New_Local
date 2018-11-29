package com.newlocal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import com.newlocal.domain.Warehouse;
import com.newlocal.service.WarehouseQueryService;
import com.newlocal.service.WarehouseService;
import com.newlocal.service.dto.WarehouseCriteria;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Warehouse.
 */
@RestController
@RequestMapping("/api")
public class WarehouseResource {

    private final Logger log = LoggerFactory.getLogger(WarehouseResource.class);

    private static final String ENTITY_NAME = "warehouse";

    private WarehouseService warehouseService;

    private WarehouseQueryService warehouseQueryService;

    public WarehouseResource(WarehouseService warehouseService, WarehouseQueryService warehouseQueryService) {
        this.warehouseService = warehouseService;
        this.warehouseQueryService = warehouseQueryService;
    }

    /**
     * POST  /warehouses : Create a new warehouse.
     *
     * @param warehouse the warehouse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new warehouse, or with status 400 (Bad Request) if the warehouse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/warehouses")
    @Timed
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) throws URISyntaxException {
        log.debug("REST request to save Warehouse : {}", warehouse);
        if (warehouse.getId() != null) {
            throw new BadRequestAlertException("A new warehouse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Warehouse result = warehouseService.save(warehouse);
        return ResponseEntity.created(new URI("/api/warehouses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /warehouses : Updates an existing warehouse.
     *
     * @param warehouse the warehouse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated warehouse,
     * or with status 400 (Bad Request) if the warehouse is not valid,
     * or with status 500 (Internal Server Error) if the warehouse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/warehouses")
    @Timed
    public ResponseEntity<Warehouse> updateWarehouse(@Valid @RequestBody Warehouse warehouse) throws URISyntaxException {
        log.debug("REST request to update Warehouse : {}", warehouse);
        if (warehouse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Warehouse result = warehouseService.save(warehouse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, warehouse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /warehouses : get all the warehouses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of warehouses in body
     */
    @GetMapping("/warehouses")
    @Timed
    public ResponseEntity<List<Warehouse>> getAllWarehouses(WarehouseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Warehouses by criteria: {}", criteria);
        Page<Warehouse> page = warehouseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/warehouses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /warehouses/count : count all the warehouses.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/warehouses/count")
    @Timed
    public ResponseEntity<Long> countWarehouses(WarehouseCriteria criteria) {
        log.debug("REST request to count Warehouses by criteria: {}", criteria);
        return ResponseEntity.ok().body(warehouseQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /warehouses/:id : get the "id" warehouse.
     *
     * @param id the id of the warehouse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the warehouse, or with status 404 (Not Found)
     */
    @GetMapping("/warehouses/{id}")
    @Timed
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable Long id) {
        log.debug("REST request to get Warehouse : {}", id);
        Optional<Warehouse> warehouse = warehouseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouse);
    }

    /**
     * DELETE  /warehouses/:id : delete the "id" warehouse.
     *
     * @param id the id of the warehouse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/warehouses/{id}")
    @Timed
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        log.debug("REST request to delete Warehouse : {}", id);
        warehouseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/warehouses?query=:query : search for the warehouse corresponding
     * to the query.
     *
     * @param query the query of the warehouse search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/warehouses")
    @Timed
    public ResponseEntity<List<Warehouse>> searchWarehouses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Warehouses for query {}", query);
        Page<Warehouse> page = warehouseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/warehouses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
