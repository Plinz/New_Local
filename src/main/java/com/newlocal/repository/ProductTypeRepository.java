package com.newlocal.repository;

import com.newlocal.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ProductType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long>, JpaSpecificationExecutor<ProductType> {

    @Query(value = "select distinct product_type from ProductType product_type left join fetch product_type.images",
        countQuery = "select count(distinct product_type) from ProductType product_type")
    Page<ProductType> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct product_type from ProductType product_type left join fetch product_type.images")
    List<ProductType> findAllWithEagerRelationships();

    @Query("select product_type from ProductType product_type left join fetch product_type.images where product_type.id =:id")
    Optional<ProductType> findOneWithEagerRelationships(@Param("id") Long id);

}
