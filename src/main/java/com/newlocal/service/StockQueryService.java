package com.newlocal.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.newlocal.domain.Stock;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.StockRepository;
import com.newlocal.repository.search.StockSearchRepository;
import com.newlocal.service.dto.StockCriteria;
import com.newlocal.service.dto.StockDTO;
import com.newlocal.service.mapper.StockMapper;

/**
 * Service for executing complex queries for Stock entities in the database.
 * The main input is a {@link StockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockDTO} or a {@link Page} of {@link StockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockQueryService extends QueryService<Stock> {

    private final Logger log = LoggerFactory.getLogger(StockQueryService.class);

    private StockRepository stockRepository;

    private StockMapper stockMapper;

    private StockSearchRepository stockSearchRepository;

    public StockQueryService(StockRepository stockRepository, StockMapper stockMapper, StockSearchRepository stockSearchRepository) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StockDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockDTO> findByCriteria(StockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.findAll(specification).stream()
        		.map(StockDTO::new).collect(Collectors.toList());
    }

    /**
     * Return a {@link Page} of {@link StockDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockDTO> findByCriteria(StockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.findAll(specification, page)
            .map(StockDTO::new);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.count(specification);
    }

    /**
     * Function to convert StockCriteria to a {@link Specification}
     */
    private Specification<Stock> createSpecification(StockCriteria criteria) {
        Specification<Stock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Stock_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Stock_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Stock_.description));
            }
            if (criteria.getQuantityInit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityInit(), Stock_.quantityInit));
            }
            if (criteria.getQuantityRemaining() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityRemaining(), Stock_.quantityRemaining));
            }
            if (criteria.getPriceUnit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriceUnit(), Stock_.priceUnit));
            }
            if (criteria.getOnSaleDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOnSaleDate(), Stock_.onSaleDate));
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), Stock_.expiryDate));
            }
            if (criteria.getBio() != null) {
                specification = specification.and(buildSpecification(criteria.getBio(), Stock_.bio));
            }
            if (criteria.getAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getAvailable(), Stock_.available));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(Stock_.image, JoinType.LEFT).get(Image_.id)));
            }
            if (criteria.getProductTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductTypeId(),
                    root -> root.join(Stock_.productType, JoinType.LEFT).get(ProductType_.id)));
            }
            if (criteria.getHoldingId() != null) {
                specification = specification.and(buildSpecification(criteria.getHoldingId(),
                    root -> root.join(Stock_.holding, JoinType.LEFT).get(Holding_.id)));
            }
            if (criteria.getSellerId() != null) {
                specification = specification.and(buildSpecification(criteria.getSellerId(),
                    root -> root.join(Stock_.seller, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getWarehouseId() != null) {
                specification = specification.and(buildSpecification(criteria.getWarehouseId(),
                    root -> root.join(Stock_.warehouse, JoinType.LEFT).get(Warehouse_.id)));
            }
        }
        return specification;
    }
}
