package com.newlocal.repository;

import com.newlocal.domain.Holding;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Holding entity.
 */
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long>, JpaSpecificationExecutor<Holding> {

    @Query("select holding from Holding holding where holding.owner.login = ?#{principal.username}")
    List<Holding> findByOwnerIsCurrentUser();

}
