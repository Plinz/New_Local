package com.newlocal.service;

import com.newlocal.domain.Purchase;
import com.newlocal.repository.PurchaseRepository;
import com.newlocal.repository.search.PurchaseSearchRepository;
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
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Purchase> findAll(Pageable pageable) {
        log.debug("Request to get all Purchases");
        return purchaseRepository.findAll(pageable);
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
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Purchase> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Purchases for query {}", query);
        return purchaseSearchRepository.search(queryStringQuery(query), pageable);
    }
    
    @Transactional(readOnly = true)
    public List<Purchase> getPStock(Long id) {
        return purchaseRepository.getPStock(id);
    }

    @Transactional(readOnly = true)
    public List<Purchase> findByClientIsCurrentUser() {
        return purchaseRepository.findByClientIsCurrentUser();
    }
}
