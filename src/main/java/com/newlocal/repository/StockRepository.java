package com.newlocal.repository;

import com.newlocal.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    @Query("select stock from Stock stock where stock.seller.login = ?#{principal.username}")
    List<Stock> findBySellerIsCurrentUser();

    @Query(value = "select distinct stock from Stock stock left join fetch stock.images",
        countQuery = "select count(distinct stock) from Stock stock")
    Page<Stock> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct stock from Stock stock left join fetch stock.images")
    List<Stock> findAllWithEagerRelationships();

    @Query("select stock from Stock stock left join fetch stock.images where stock.id =:id")
    Optional<Stock> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select stock from Stock stock where stock.bio = true")
    List<Stock> getProductBio();

    @Query("select stock from Stock stock")
    List<Stock> findAllStocks(Sort sort);

    @Query(value="select * from stock where quantity_init-quantity_remaining = (SELECT MAX(quantity_init-quantity_remaining) FROM stock )",nativeQuery = true)
    List<Stock> getBestPurchase();

    @Query(value="SELECT * FROM ((STOCK JOIN GRADE ON STOCK.PRODUCT_TYPE_ID=GRADE.PRODUCT_TYPE_ID) JOIN PRODUCT_TYPE ON STOCK.PRODUCT_TYPE_ID=PRODUCT_TYPE.ID) WHERE GRADE.GRADE='5'",nativeQuery = true)
    List<Stock> getBestGrade();

}
