package com.newlocal.repository;

import com.newlocal.domain.PurchaseDone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PurchaseDone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseDoneRepository extends JpaRepository<PurchaseDone, Long>, JpaSpecificationExecutor<PurchaseDone> {

    @Query("select purchase_done from PurchaseDone purchase_done where purchase_done.client.login = ?#{principal.username}")
    List<PurchaseDone> findByClientIsCurrentUser();

}
