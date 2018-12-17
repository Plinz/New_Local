package com.newlocal.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newlocal.domain.Holding;
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
import com.newlocal.service.dto.HoldingDTO;
import com.newlocal.service.mapper.HoldingMapper;

/**
 * Service Implementation for managing Holding.
 */
@Service
@Transactional
public class HoldingService {

    private final Logger log = LoggerFactory.getLogger(HoldingService.class);

    private HoldingRepository holdingRepository;

    private HoldingMapper holdingMapper;

    private HoldingSearchRepository holdingSearchRepository;

    public HoldingService(HoldingRepository holdingRepository, HoldingMapper holdingMapper, HoldingSearchRepository holdingSearchRepository) {
        this.holdingRepository = holdingRepository;
        this.holdingMapper = holdingMapper;
        this.holdingSearchRepository = holdingSearchRepository;
    }

    /**
     * Save a holding.
     *
     * @param holdingDTO the entity to save
     * @return the persisted entity
     */
    public HoldingDTO save(HoldingDTO holdingDTO) {
        log.debug("Request to save Holding : {}", holdingDTO);

        Holding holding = holdingMapper.toEntity(holdingDTO);
        holding = holdingRepository.save(holding);
        HoldingDTO result = holdingMapper.toDto(holding);
        holdingSearchRepository.save(holding);
        return result;
    }

    /**
     * Get all the holdings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HoldingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Holdings");
        return holdingRepository.findAll(pageable)
            .map(HoldingDTO::new);
    }


    /**
     * Get one holding by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<HoldingDTO> findOne(Long id) {
        log.debug("Request to get Holding : {}", id);
        return holdingRepository.findById(id)
            .map(HoldingDTO::new);
    }
    
    /**
     * Get multiple holdings of current user.
     *
     * @return the entities list
     */
    @Transactional(readOnly = true)
    public List<HoldingDTO> findByCurrentUser() {
        log.debug("Request to get Holdings of the current user");
        return holdingRepository.findByOwnerIsCurrentUser().stream()
        		.map(HoldingDTO::new).collect(Collectors.toList());
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
    public Page<HoldingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Holdings for query {}", query);
        return holdingSearchRepository.search(queryStringQuery(query), pageable)
            .map(HoldingDTO::new);
    }
}
