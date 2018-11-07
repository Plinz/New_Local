package com.newlocal.repository;

import com.newlocal.domain.PurchaseDone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseDone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseDoneRepository extends JpaRepository<PurchaseDone, Long> {

}
