package com.newlocal.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.newlocal.domain.Purchase;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.PurchaseRepository;
import com.newlocal.repository.search.PurchaseSearchRepository;
import com.newlocal.service.dto.PurchaseCriteria;

/**
 * Service for executing complex queries for Purchase entities in the database.
 * The main input is a {@link PurchaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Purchase} or a {@link Page} of {@link Purchase} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseQueryService extends QueryService<Purchase> {

    private final Logger log = LoggerFactory.getLogger(PurchaseQueryService.class);

    private PurchaseRepository purchaseRepository;

//    private PurchaseSearchRepository purchaseSearchRepository;

    public PurchaseQueryService(PurchaseRepository purchaseRepository, PurchaseSearchRepository purchaseSearchRepository) {
        this.purchaseRepository = purchaseRepository;
//        this.purchaseSearchRepository = purchaseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Purchase} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Purchase> findByCriteria(PurchaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Purchase> specification = createSpecification(criteria);
        return purchaseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Purchase} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Purchase> findByCriteria(PurchaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Purchase> specification = createSpecification(criteria);
        return purchaseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Purchase> specification = createSpecification(criteria);
        return purchaseRepository.count(specification);
    }

    /**
     * Function to convert PurchaseCriteria to a {@link Specification}
     */
    private Specification<Purchase> createSpecification(PurchaseCriteria criteria) {
        Specification<Purchase> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Purchase_.id));
            }
            if (criteria.getSaleDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaleDate(), Purchase_.saleDate));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Purchase_.quantity));
            }
            if (criteria.getWithdraw() != null) {
                specification = specification.and(buildSpecification(criteria.getWithdraw(), Purchase_.withdraw));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(Purchase_.stock, JoinType.LEFT).get(Stock_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Purchase_.client, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
