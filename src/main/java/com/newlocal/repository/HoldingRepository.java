package com.newlocal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.newlocal.domain.Holding;

/**
 * Spring Data  repository for the Holding entity.
 */
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long>, JpaSpecificationExecutor<Holding> {

    @Query("select holding from Holding holding where holding.owner.login = ?#{principal.username}")
    List<Holding> findByOwnerIsCurrentUser();

}
