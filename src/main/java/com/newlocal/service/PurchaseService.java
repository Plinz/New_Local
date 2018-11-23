package com.newlocal.service;

import com.newlocal.domain.Purchase;
import com.newlocal.repository.PurchaseRepository;
import com.newlocal.repository.search.PurchaseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Purchase.
 */
@Service
@Transactional
public class PurchaseService {

    private final Logger log = LoggerFactory.getLogger(PurchaseService.class);

    private PurchaseRepository purchaseRepository;

    private PurchaseSearchRepository purchaseSearchRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseSearchRepository purchaseSearchRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseSearchRepository = purchaseSearchRepository;
    }

    /**
     * Save a purchase.
     *
     * @param purchase the entity to save
     * @return the persisted entity
     */
    public Purchase save(Purchase purchase) {
        log.debug("Request to save Purchase : {}", purchase);
        Purchase result = purchaseRepository.save(purchase);
        purchaseSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the purchases.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Purchase> findAll() {
        log.debug("Request to get all Purchases");
        return purchaseRepository.findAll();
    }


    /**
     * Get one purchase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Purchase> findOne(Long id) {
        log.debug("Request to get Purchase : {}", id);
        return purchaseRepository.findById(id);
    }

    /**
     * Delete the purchase by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Purchase : {}", id);
        purchaseRepository.deleteById(id);
        purchaseSearchRepository.deleteById(id);
    }

    /**
     * Search for the purchase corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Purchase> search(String query) {
        log.debug("Request to search Purchases for query {}", query);
        return StreamSupport
            .stream(purchaseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
