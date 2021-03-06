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

import com.newlocal.domain.Grade;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.GradeRepository;
import com.newlocal.repository.search.GradeSearchRepository;
import com.newlocal.service.dto.GradeCriteria;

/**
 * Service for executing complex queries for Grade entities in the database.
 * The main input is a {@link GradeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Grade} or a {@link Page} of {@link Grade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GradeQueryService extends QueryService<Grade> {

    private final Logger log = LoggerFactory.getLogger(GradeQueryService.class);

    private GradeRepository gradeRepository;

//    private GradeSearchRepository gradeSearchRepository;

    public GradeQueryService(GradeRepository gradeRepository, GradeSearchRepository gradeSearchRepository) {
        this.gradeRepository = gradeRepository;
//        this.gradeSearchRepository = gradeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Grade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Grade> findByCriteria(GradeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Grade> specification = createSpecification(criteria);
        return gradeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Grade} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Grade> findByCriteria(GradeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Grade> specification = createSpecification(criteria);
        return gradeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GradeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Grade> specification = createSpecification(criteria);
        return gradeRepository.count(specification);
    }

    /**
     * Function to convert GradeCriteria to a {@link Specification}
     */
    private Specification<Grade> createSpecification(GradeCriteria criteria) {
        Specification<Grade> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Grade_.id));
            }
            if (criteria.getGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrade(), Grade_.grade));
            }
            if (criteria.getNbVoter() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbVoter(), Grade_.nbVoter));
            }
            if (criteria.getSellerId() != null) {
                specification = specification.and(buildSpecification(criteria.getSellerId(),
                    root -> root.join(Grade_.seller, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getProductTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductTypeId(),
                    root -> root.join(Grade_.productType, JoinType.LEFT).get(ProductType_.id)));
            }
        }
        return specification;
    }
}
