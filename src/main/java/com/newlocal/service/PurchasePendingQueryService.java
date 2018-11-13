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

import com.newlocal.domain.PurchasePending;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.PurchasePendingRepository;
import com.newlocal.repository.search.PurchasePendingSearchRepository;
import com.newlocal.service.dto.PurchasePendingCriteria;

/**
 * Service for executing complex queries for PurchasePending entities in the database.
 * The main input is a {@link PurchasePendingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchasePending} or a {@link Page} of {@link PurchasePending} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchasePendingQueryService extends QueryService<PurchasePending> {

    private final Logger log = LoggerFactory.getLogger(PurchasePendingQueryService.class);

    private PurchasePendingRepository purchasePendingRepository;

    private PurchasePendingSearchRepository purchasePendingSearchRepository;

    public PurchasePendingQueryService(PurchasePendingRepository purchasePendingRepository, PurchasePendingSearchRepository purchasePendingSearchRepository) {
        this.purchasePendingRepository = purchasePendingRepository;
        this.purchasePendingSearchRepository = purchasePendingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PurchasePending} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchasePending> findByCriteria(PurchasePendingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchasePending> specification = createSpecification(criteria);
        return purchasePendingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PurchasePending} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchasePending> findByCriteria(PurchasePendingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchasePending> specification = createSpecification(criteria);
        return purchasePendingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchasePendingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchasePending> specification = createSpecification(criteria);
        return purchasePendingRepository.count(specification);
    }

    /**
     * Function to convert PurchasePendingCriteria to a {@link Specification}
     */
    private Specification<PurchasePending> createSpecification(PurchasePendingCriteria criteria) {
        Specification<PurchasePending> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchasePending_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), PurchasePending_.quantity));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(PurchasePending_.stock, JoinType.LEFT).get(Stock_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(PurchasePending_.client, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
