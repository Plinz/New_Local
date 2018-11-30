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

import com.newlocal.domain.Holding;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
import com.newlocal.service.dto.HoldingCriteria;

/**
 * Service for executing complex queries for Holding entities in the database.
 * The main input is a {@link HoldingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Holding} or a {@link Page} of {@link Holding} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HoldingQueryService extends QueryService<Holding> {

    private final Logger log = LoggerFactory.getLogger(HoldingQueryService.class);

    private HoldingRepository holdingRepository;

//    private HoldingSearchRepository holdingSearchRepository;

    public HoldingQueryService(HoldingRepository holdingRepository, HoldingSearchRepository holdingSearchRepository) {
        this.holdingRepository = holdingRepository;
//        this.holdingSearchRepository = holdingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Holding} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Holding> findByCriteria(HoldingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Holding> specification = createSpecification(criteria);
        return holdingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Holding} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Holding> findByCriteria(HoldingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Holding> specification = createSpecification(criteria);
        return holdingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HoldingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Holding> specification = createSpecification(criteria);
        return holdingRepository.count(specification);
    }

    /**
     * Function to convert HoldingCriteria to a {@link Specification}
     */
    private Specification<Holding> createSpecification(HoldingCriteria criteria) {
        Specification<Holding> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Holding_.id));
            }
            if (criteria.getSiret() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSiret(), Holding_.siret));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Holding_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Holding_.description));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(Holding_.image, JoinType.LEFT).get(Image_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Holding_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Holding_.owner, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
