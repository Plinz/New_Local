package com.newlocal.repository;

import com.newlocal.domain.Holding;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Holding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

}
