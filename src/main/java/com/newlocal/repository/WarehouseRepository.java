package com.newlocal.repository;

import com.newlocal.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Warehouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

    @Query(value = "select distinct warehouse from Warehouse warehouse left join fetch warehouse.images",
        countQuery = "select count(distinct warehouse) from Warehouse warehouse")
    Page<Warehouse> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct warehouse from Warehouse warehouse left join fetch warehouse.images")
    List<Warehouse> findAllWithEagerRelationships();

    @Query("select warehouse from Warehouse warehouse left join fetch warehouse.images where warehouse.id =:id")
    Optional<Warehouse> findOneWithEagerRelationships(@Param("id") Long id);

}
