package com.newlocal.repository;

import com.newlocal.domain.Grade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Grade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {

    @Query("select grade from Grade grade where grade.user.login = ?#{principal.username}")
    List<Grade> findByUserIsCurrentUser();

}