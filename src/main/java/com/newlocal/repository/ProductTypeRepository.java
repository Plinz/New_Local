package com.newlocal.repository;

import com.newlocal.domain.ProductType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ProductType entity.
 */
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long>, JpaSpecificationExecutor<ProductType> {

    @Query("select s.productType from Stock s where s.seller.login = ?#{principal.username}")
    List<ProductType> findByClientIsCurrentUser();
}
