package com.newlocal.service;

import com.newlocal.domain.Warehouse;
import com.newlocal.repository.WarehouseRepository;
import com.newlocal.repository.search.WarehouseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Warehouse.
 */
@Service
@Transactional
public class WarehouseService {

    private final Logger log = LoggerFactory.getLogger(WarehouseService.class);

    private WarehouseRepository warehouseRepository;

    private WarehouseSearchRepository warehouseSearchRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, WarehouseSearchRepository warehouseSearchRepository) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseSearchRepository = warehouseSearchRepository;
    }

    /**
     * Save a warehouse.
     *
     * @param warehouse the entity to save
     * @return the persisted entity
     */
    public Warehouse save(Warehouse warehouse) {
        log.debug("Request to save Warehouse : {}", warehouse);
        Warehouse result = warehouseRepository.save(warehouse);
        warehouseSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the warehouses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Warehouse> findAll(Pageable pageable) {
        log.debug("Request to get all Warehouses");
        return warehouseRepository.findAll(pageable);
    }


    /**
     * Get one warehouse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Warehouse> findOne(Long id) {
        log.debug("Request to get Warehouse : {}", id);
        return warehouseRepository.findById(id);
    }

    /**
     * Delete the warehouse by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Warehouse : {}", id);
        warehouseRepository.deleteById(id);
        warehouseSearchRepository.deleteById(id);
    }

    /**
     * Search for the warehouse corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Warehouse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Warehouses for query {}", query);
        return warehouseSearchRepository.search(queryStringQuery(query), pageable);    }
}
