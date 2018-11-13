package com.newlocal.repository;

import com.newlocal.domain.PurchasePending;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PurchasePending entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasePendingRepository extends JpaRepository<PurchasePending, Long>, JpaSpecificationExecutor<PurchasePending> {

    @Query("select purchase_pending from PurchasePending purchase_pending where purchase_pending.client.login = ?#{principal.username}")
    List<PurchasePending> findByClientIsCurrentUser();

}
