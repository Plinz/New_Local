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

import com.newlocal.domain.Cart;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.CartRepository;
import com.newlocal.repository.search.CartSearchRepository;
import com.newlocal.service.dto.CartCriteria;

/**
 * Service for executing complex queries for Cart entities in the database.
 * The main input is a {@link CartCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cart} or a {@link Page} of {@link Cart} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CartQueryService extends QueryService<Cart> {

    private final Logger log = LoggerFactory.getLogger(CartQueryService.class);

    private CartRepository cartRepository;

//    private CartSearchRepository cartSearchRepository;

    public CartQueryService(CartRepository cartRepository, CartSearchRepository cartSearchRepository) {
        this.cartRepository = cartRepository;
//        this.cartSearchRepository = cartSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Cart} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cart> findByCriteria(CartCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cart> specification = createSpecification(criteria);
        return cartRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cart} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cart> findByCriteria(CartCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cart> specification = createSpecification(criteria);
        return cartRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CartCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cart> specification = createSpecification(criteria);
        return cartRepository.count(specification);
    }

    /**
     * Function to convert CartCriteria to a {@link Specification}
     */
    private Specification<Cart> createSpecification(CartCriteria criteria) {
        Specification<Cart> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cart_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), Cart_.quantity));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(Cart_.stock, JoinType.LEFT).get(Stock_.id)));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Cart_.client, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
