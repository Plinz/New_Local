package com.newlocal.service;

import com.newlocal.domain.PurchasePending;
import com.newlocal.repository.PurchasePendingRepository;
import com.newlocal.repository.search.PurchasePendingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PurchasePending.
 */
@Service
@Transactional
public class PurchasePendingService {

    private final Logger log = LoggerFactory.getLogger(PurchasePendingService.class);

    private PurchasePendingRepository purchasePendingRepository;

    private PurchasePendingSearchRepository purchasePendingSearchRepository;

    public PurchasePendingService(PurchasePendingRepository purchasePendingRepository, PurchasePendingSearchRepository purchasePendingSearchRepository) {
        this.purchasePendingRepository = purchasePendingRepository;
        this.purchasePendingSearchRepository = purchasePendingSearchRepository;
    }

    /**
     * Save a purchasePending.
     *
     * @param purchasePending the entity to save
     * @return the persisted entity
     */
    public PurchasePending save(PurchasePending purchasePending) {
        log.debug("Request to save PurchasePending : {}", purchasePending);
        PurchasePending result = purchasePendingRepository.save(purchasePending);
        purchasePendingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the purchasePendings.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PurchasePending> findAll() {
        log.debug("Request to get all PurchasePendings");
        return purchasePendingRepository.findAll();
    }


    /**
     * Get one purchasePending by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PurchasePending> findOne(Long id) {
        log.debug("Request to get PurchasePending : {}", id);
        return purchasePendingRepository.findById(id);
    }

    /**
     * Delete the purchasePending by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchasePending : {}", id);
        purchasePendingRepository.deleteById(id);
        purchasePendingSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchasePending corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PurchasePending> search(String query) {
        log.debug("Request to search PurchasePendings for query {}", query);
        return StreamSupport
            .stream(purchasePendingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
