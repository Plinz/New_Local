package com.newlocal.repository;

import com.newlocal.domain.Grade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Grade entity.
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {

    @Query("select grade from Grade grade where grade.seller.login = ?#{principal.username}")
    List<Grade> findBySellerIsCurrentUser();

}
