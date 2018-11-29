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

import com.newlocal.domain.Image;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.ImageRepository;
import com.newlocal.repository.search.ImageSearchRepository;
import com.newlocal.service.dto.ImageCriteria;

/**
 * Service for executing complex queries for Image entities in the database.
 * The main input is a {@link ImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Image} or a {@link Page} of {@link Image} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageQueryService extends QueryService<Image> {

    private final Logger log = LoggerFactory.getLogger(ImageQueryService.class);

    private ImageRepository imageRepository;

    private ImageSearchRepository imageSearchRepository;

    public ImageQueryService(ImageRepository imageRepository, ImageSearchRepository imageSearchRepository) {
        this.imageRepository = imageRepository;
        this.imageSearchRepository = imageSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Image} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Image> findByCriteria(ImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Image> specification = createSpecification(criteria);
        return imageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Image} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Image> findByCriteria(ImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Image> specification = createSpecification(criteria);
        return imageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Image> specification = createSpecification(criteria);
        return imageRepository.count(specification);
    }

    /**
     * Function to convert ImageCriteria to a {@link Specification}
     */
    private Specification<Image> createSpecification(ImageCriteria criteria) {
        Specification<Image> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Image_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Image_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Image_.description));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(Image_.stocks, JoinType.LEFT).get(Stock_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Image_.categories, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getProductTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductTypeId(),
                    root -> root.join(Image_.productTypes, JoinType.LEFT).get(ProductType_.id)));
            }
            if (criteria.getHoldingId() != null) {
                specification = specification.and(buildSpecification(criteria.getHoldingId(),
                    root -> root.join(Image_.holdings, JoinType.LEFT).get(Holding_.id)));
            }
            if (criteria.getWarehouseId() != null) {
                specification = specification.and(buildSpecification(criteria.getWarehouseId(),
                    root -> root.join(Image_.warehouses, JoinType.LEFT).get(Warehouse_.id)));
            }
        }
        return specification;
    }
}
