package com.newlocal.service;

import com.newlocal.domain.Holding;
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Holding.
 */
@Service
@Transactional
public class HoldingService {

    private final Logger log = LoggerFactory.getLogger(HoldingService.class);

    private HoldingRepository holdingRepository;

    private HoldingSearchRepository holdingSearchRepository;

    public HoldingService(HoldingRepository holdingRepository, HoldingSearchRepository holdingSearchRepository) {
        this.holdingRepository = holdingRepository;
        this.holdingSearchRepository = holdingSearchRepository;
    }

    /**
     * Save a holding.
     *
     * @param holding the entity to save
     * @return the persisted entity
     */
    public Holding save(Holding holding) {
        log.debug("Request to save Holding : {}", holding);
        Holding result = holdingRepository.save(holding);
        holdingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the holdings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Holding> findAll(Pageable pageable) {
        log.debug("Request to get all Holdings");
        return holdingRepository.findAll(pageable);
    }

    /**
     * Get all the Holding with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Holding> findAllWithEagerRelationships(Pageable pageable) {
        return holdingRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one holding by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Holding> findOne(Long id) {
        log.debug("Request to get Holding : {}", id);
        return holdingRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Get multiple holdings of current user.
     *
     * @return the entities list
     */
    @Transactional(readOnly = true)
    public List<Holding> findByCurrentUser() {
        log.debug("Request to get Holdings of the current user");
        return holdingRepository.findByOwnerIsCurrentUser();
    }

    /**
     * Delete the holding by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Holding : {}", id);
        holdingRepository.deleteById(id);
        holdingSearchRepository.deleteById(id);
    }

    /**
     * Search for the holding corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Holding> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Holdings for query {}", query);
        return holdingSearchRepository.search(queryStringQuery(query), pageable);    }
}
