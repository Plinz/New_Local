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

import com.newlocal.domain.ProductType;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.ProductTypeRepository;
import com.newlocal.repository.search.ProductTypeSearchRepository;
import com.newlocal.service.dto.ProductTypeCriteria;

/**
 * Service for executing complex queries for ProductType entities in the database.
 * The main input is a {@link ProductTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductType} or a {@link Page} of {@link ProductType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductTypeQueryService extends QueryService<ProductType> {

    private final Logger log = LoggerFactory.getLogger(ProductTypeQueryService.class);

    private ProductTypeRepository productTypeRepository;

//    private ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeQueryService(ProductTypeRepository productTypeRepository, ProductTypeSearchRepository productTypeSearchRepository) {
        this.productTypeRepository = productTypeRepository;
//        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductType> findByCriteria(ProductTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProductType} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductType> findByCriteria(ProductTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.count(specification);
    }

    /**
     * Function to convert ProductTypeCriteria to a {@link Specification}
     */
    private Specification<ProductType> createSpecification(ProductTypeCriteria criteria) {
        Specification<ProductType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProductType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductType_.description));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(ProductType_.image, JoinType.LEFT).get(Image_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(ProductType_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
