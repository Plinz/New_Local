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

import com.newlocal.domain.PurchaseDone;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.PurchaseDoneRepository;
import com.newlocal.repository.search.PurchaseDoneSearchRepository;
import com.newlocal.service.dto.PurchaseDoneCriteria;

/**
 * Service for executing complex queries for PurchaseDone entities in the database.
 * The main input is a {@link PurchaseDoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PurchaseDone} or a {@link Page} of {@link PurchaseDone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseDoneQueryService extends QueryService<PurchaseDone> {

    private final Logger log = LoggerFactory.getLogger(PurchaseDoneQueryService.class);

    private PurchaseDoneRepository purchaseDoneRepository;

    private PurchaseDoneSearchRepository purchaseDoneSearchRepository;

    public PurchaseDoneQueryService(PurchaseDoneRepository purchaseDoneRepository, PurchaseDoneSearchRepository purchaseDoneSearchRepository) {
        this.purchaseDoneRepository = purchaseDoneRepository;
        this.purchaseDoneSearchRepository = purchaseDoneSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PurchaseDone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseDone> findByCriteria(PurchaseDoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PurchaseDone> specification = createSpecification(criteria);
        return purchaseDoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PurchaseDone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseDone> findByCriteria(PurchaseDoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PurchaseDone> specification = createSpecification(criteria);
        return purchaseDoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PurchaseDoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PurchaseDone> specification = createSpecification(criteria);
        return purchaseDoneRepository.count(specification);
    }

    /**
     * Function to convert PurchaseDoneCriteria to a {@link Specification}
     */
    private Specification<PurchaseDone> createSpecification(PurchaseDoneCriteria criteria) {
        Specification<PurchaseDone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PurchaseDone_.id));
            }
            if (criteria.getSaleDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaleDate(), PurchaseDone_.saleDate));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), PurchaseDone_.quantity));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(PurchaseDone_.stock, JoinType.LEFT).get(Stock_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(PurchaseDone_.client, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
