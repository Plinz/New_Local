package com.newlocal.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.newlocal.domain.Stock;

/**
 * Spring Data  repository for the Stock entity.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    @Query("select stock from Stock stock where stock.seller.login = ?#{principal.username}")
    List<Stock> findBySellerIsCurrentUser();

    @Query("select stock from Stock stock where stock.bio = true")
    List<Stock> getProductBio();

    @Query("select stock from Stock stock")
    List<Stock> findAllStocks(Sort sort);

    @Query(value="select * from stock where quantity_init-quantity_remaining = (SELECT MAX(quantity_init-quantity_remaining) FROM stock )",nativeQuery = true)
    List<Stock> getBestPurchase();

    @Query(value="SELECT * FROM ((STOCK JOIN GRADE ON STOCK.PRODUCT_TYPE_ID=GRADE.PRODUCT_TYPE_ID) JOIN PRODUCT_TYPE ON STOCK.PRODUCT_TYPE_ID=PRODUCT_TYPE.ID) WHERE GRADE.GRADE='5'",nativeQuery = true)
    List<Stock> getBestGrade();

    @Query("select stock from Stock stock where stock.productType.category.name=:name")
    List<Stock> getStockCat(@Param("name") String name);

}
