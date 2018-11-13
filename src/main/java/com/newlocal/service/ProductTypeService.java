package com.newlocal.service;

import com.newlocal.domain.ProductType;
import com.newlocal.repository.ProductTypeRepository;
import com.newlocal.repository.search.ProductTypeSearchRepository;
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
 * Service Implementation for managing ProductType.
 */
@Service
@Transactional
public class ProductTypeService {

    private final Logger log = LoggerFactory.getLogger(ProductTypeService.class);

    private ProductTypeRepository productTypeRepository;

    private ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeSearchRepository productTypeSearchRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    /**
     * Save a productType.
     *
     * @param productType the entity to save
     * @return the persisted entity
     */
    public ProductType save(ProductType productType) {
        log.debug("Request to save ProductType : {}", productType);
        ProductType result = productTypeRepository.save(productType);
        productTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the productTypes.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProductType> findAll() {
        log.debug("Request to get all ProductTypes");
        return productTypeRepository.findAll();
    }


    /**
     * Get one productType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductType> findOne(Long id) {
        log.debug("Request to get ProductType : {}", id);
        return productTypeRepository.findById(id);
    }

    /**
     * Delete the productType by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductType : {}", id);
        productTypeRepository.deleteById(id);
        productTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the productType corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProductType> search(String query) {
        log.debug("Request to search ProductTypes for query {}", query);
        return StreamSupport
            .stream(productTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
