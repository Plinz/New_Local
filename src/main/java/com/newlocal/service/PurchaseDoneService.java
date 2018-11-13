package com.newlocal.service;

import com.newlocal.domain.PurchaseDone;
import com.newlocal.repository.PurchaseDoneRepository;
import com.newlocal.repository.search.PurchaseDoneSearchRepository;
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
 * Service Implementation for managing PurchaseDone.
 */
@Service
@Transactional
public class PurchaseDoneService {

    private final Logger log = LoggerFactory.getLogger(PurchaseDoneService.class);

    private PurchaseDoneRepository purchaseDoneRepository;

    private PurchaseDoneSearchRepository purchaseDoneSearchRepository;

    public PurchaseDoneService(PurchaseDoneRepository purchaseDoneRepository, PurchaseDoneSearchRepository purchaseDoneSearchRepository) {
        this.purchaseDoneRepository = purchaseDoneRepository;
        this.purchaseDoneSearchRepository = purchaseDoneSearchRepository;
    }

    /**
     * Save a purchaseDone.
     *
     * @param purchaseDone the entity to save
     * @return the persisted entity
     */
    public PurchaseDone save(PurchaseDone purchaseDone) {
        log.debug("Request to save PurchaseDone : {}", purchaseDone);
        PurchaseDone result = purchaseDoneRepository.save(purchaseDone);
        purchaseDoneSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the purchaseDones.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PurchaseDone> findAll() {
        log.debug("Request to get all PurchaseDones");
        return purchaseDoneRepository.findAll();
    }


    /**
     * Get one purchaseDone by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseDone> findOne(Long id) {
        log.debug("Request to get PurchaseDone : {}", id);
        return purchaseDoneRepository.findById(id);
    }

    /**
     * Delete the purchaseDone by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseDone : {}", id);
        purchaseDoneRepository.deleteById(id);
        purchaseDoneSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchaseDone corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PurchaseDone> search(String query) {
        log.debug("Request to search PurchaseDones for query {}", query);
        return StreamSupport
            .stream(purchaseDoneSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
