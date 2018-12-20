package com.newlocal.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newlocal.domain.Stock;
import com.newlocal.domain.User;
import com.newlocal.domain.Warehouse;
import com.newlocal.domain.Holding;

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

    @Query(value="select min(price_unit) as minPriceUnit, avg(price_unit) as avgPriceUnit, max(price_unit) as maxPriceUnit " +
        "from stock " +
        "where product_type_id = :productTypeId " +
        "and bio = :bio " +
        "and available = true " +
        "and expiry_date > CURDATE() " +
        "and quantity_remaining > 0",nativeQuery = true)
    List<Object[]> getStatsStock(@Param("productTypeId") Long productTypeId, @Param("bio") Boolean bio);

    // POSTGRE
    /*"select min(price_unit) as minPriceUnit, avg(price_unit) as avgPriceUnit, quantile(price_unit, 0.5) as medianPriceUnit, max(price_unit) as maxPriceUnit " +
        "from stock " +
        "where product_type_id = :productTypeId " +
        "and bio = :bio " +
        "and available = true " +
        "and expiry_date > current_date " +
        "and quantity_remaining > 0"*/

    //MYSQL
    /*"select min(price_unit) as minPriceUnit, avg(price_unit) as avgPriceUnit, max(price_unit) as maxPriceUnit " +
        "from stock " +
        "where product_type_id = :productTypeId " +
        "and bio = :bio " +
        "and available = true " +
        "and expiry_date > CURDATE() " +
        "and quantity_remaining > 0"*/

    @Query("select stock from Stock stock where stock.productType.category.name=:name")
    List<Stock> getStockCat(@Param("name") String name);

    @Query("select distinct stock.seller from Stock stock")
    List<User> allSeller();

    @Query("select distinct stock.holding from Stock stock")
    List<Holding> allHolding();

    @Query("select distinct stock.warehouse from Stock stock")
    List<Warehouse> allWarehouse();
    
    @Query("select stock from Stock stock where (stock.productType.category.name=:cat and stock.seller.lastName=:seller and stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterCatSeller(@Param("cat") String cat,@Param("seller") String seller,@Param("min") Double min,@Param("max") Double max);

    @Query("select stock from Stock stock where (stock.productType.category.name=:cat and stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterCat(@Param("cat") String cat,@Param("min") Double min,@Param("max") Double max);

    @Query("select stock from Stock stock where ( stock.seller.lastName=:seller and stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterSeller(@Param("seller") String seller,@Param("min") Double min,@Param("max") Double max);

    @Query("select stock from Stock stock where ( stock.priceUnit>=:min and stock.priceUnit<=:max )")
    List<Stock> filterPrice(@Param("min") Double min,@Param("max") Double max);

    @Query(value="select max(stock.priceUnit) as maxPriceUnit from Stock stock ")
    List<String> prixMax();

    @Query(value="select stock.quantityRemaining from Stock stock where stock.id=:id")
    List<String> getRemaning(@Param("id") Long id);
}
