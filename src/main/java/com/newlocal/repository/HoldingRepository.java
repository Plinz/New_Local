package com.newlocal.repository;

import com.newlocal.domain.Holding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Holding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long>, JpaSpecificationExecutor<Holding> {

    @Query("select holding from Holding holding where holding.owner.login = ?#{principal.username}")
    List<Holding> findByOwnerIsCurrentUser();

    @Query(value = "select distinct holding from Holding holding left join fetch holding.images",
        countQuery = "select count(distinct holding) from Holding holding")
    Page<Holding> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct holding from Holding holding left join fetch holding.images")
    List<Holding> findAllWithEagerRelationships();

    @Query("select holding from Holding holding left join fetch holding.images where holding.id =:id")
    Optional<Holding> findOneWithEagerRelationships(@Param("id") Long id);

}
