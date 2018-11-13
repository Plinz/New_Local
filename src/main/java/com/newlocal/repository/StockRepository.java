package com.newlocal.repository;

import com.newlocal.domain.Stock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    @Query("select stock from Stock stock where stock.seller.login = ?#{principal.username}")
    List<Stock> findBySellerIsCurrentUser();

}
