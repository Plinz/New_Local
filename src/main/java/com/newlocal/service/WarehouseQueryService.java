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

import com.newlocal.domain.Warehouse;
import com.newlocal.domain.*; // for static metamodels
import com.newlocal.repository.WarehouseRepository;
import com.newlocal.repository.search.WarehouseSearchRepository;
import com.newlocal.service.dto.WarehouseCriteria;

/**
 * Service for executing complex queries for Warehouse entities in the database.
 * The main input is a {@link WarehouseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Warehouse} or a {@link Page} of {@link Warehouse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WarehouseQueryService extends QueryService<Warehouse> {

    private final Logger log = LoggerFactory.getLogger(WarehouseQueryService.class);

    private WarehouseRepository warehouseRepository;

//    private WarehouseSearchRepository warehouseSearchRepository;

    public WarehouseQueryService(WarehouseRepository warehouseRepository, WarehouseSearchRepository warehouseSearchRepository) {
        this.warehouseRepository = warehouseRepository;
//        this.warehouseSearchRepository = warehouseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Warehouse} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Warehouse> findByCriteria(WarehouseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Warehouse> specification = createSpecification(criteria);
        return warehouseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Warehouse} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Warehouse> findByCriteria(WarehouseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Warehouse> specification = createSpecification(criteria);
        return warehouseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WarehouseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Warehouse> specification = createSpecification(criteria);
        return warehouseRepository.count(specification);
    }

    /**
     * Function to convert WarehouseCriteria to a {@link Specification}
     */
    private Specification<Warehouse> createSpecification(WarehouseCriteria criteria) {
        Specification<Warehouse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Warehouse_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Warehouse_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Warehouse_.description));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), Warehouse_.tel));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(Warehouse_.image, JoinType.LEFT).get(Image_.id)));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Warehouse_.location, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
