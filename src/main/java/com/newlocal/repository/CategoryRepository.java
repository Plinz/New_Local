package com.newlocal.repository;

import com.newlocal.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    @Query(value = "select distinct category from Category category left join fetch category.images",
        countQuery = "select count(distinct category) from Category category")
    Page<Category> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct category from Category category left join fetch category.images")
    List<Category> findAllWithEagerRelationships();

    @Query("select category from Category category left join fetch category.images where category.id =:id")
    Optional<Category> findOneWithEagerRelationships(@Param("id") Long id);

}
