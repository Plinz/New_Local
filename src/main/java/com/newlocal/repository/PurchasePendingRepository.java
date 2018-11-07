package com.newlocal.repository;

import com.newlocal.domain.PurchasePending;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchasePending entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasePendingRepository extends JpaRepository<PurchasePending, Long> {

}
