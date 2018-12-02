package com.newlocal.repository;

import com.newlocal.domain.Purchase;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Purchase entity.
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {

    @Query("select purchase from Purchase purchase where purchase.client.login = ?#{principal.username}")
    List<Purchase> findByClientIsCurrentUser();

    @Query("select purchase from Purchase purchase where purchase.stock.id = :id")
    List<Purchase> getPStock(@Param("id") Long id);
}
