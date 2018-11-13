package com.newlocal.service;

import com.newlocal.domain.Holding;
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
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
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Holding> findAll() {
        log.debug("Request to get all Holdings");
        return holdingRepository.findAll();
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
        return holdingRepository.findById(id);
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
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Holding> search(String query) {
        log.debug("Request to search Holdings for query {}", query);
        return StreamSupport
            .stream(holdingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
