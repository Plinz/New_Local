package com.newlocal.repository;

import com.newlocal.domain.Purchase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Purchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {

    @Query("select purchase from Purchase purchase where purchase.client.login = ?#{principal.username}")
    List<Purchase> findByClientIsCurrentUser();

}
