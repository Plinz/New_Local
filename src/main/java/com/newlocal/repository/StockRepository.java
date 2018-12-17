package com.newlocal.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.newlocal.domain.Stock;
import com.newlocal.domain.User;

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

    @Query("select distinct user from Stock")
    List<User> allSeller();
    
    @Query("select stock from Stock stock where (stock.productType.category.name=:cat and stock.seller.lastName=:seller and stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterCatSeller(@Param("cat") String cat,@Param("seller") String seller,@Param("min") Long min,@Param("max") Long max);

    @Query("select stock from Stock stock where (stock.productType.category.name=:cat and stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterCat(@Param("cat") String cat,@Param("min") Long min,@Param("max") Long max);

    @Query("select stock from Stock stock where ( stock.seller.lastName=:seller and stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterSeller(@Param("seller") String seller,@Param("min") Long min,@Param("max") Long max);

    @Query("select stock from Stock stock where ( stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterPrice(@Param("min") Long min,@Param("max") Long max);
}
