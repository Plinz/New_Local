package com.newlocal.service;

import com.newlocal.domain.Stock;
import com.newlocal.repository.StockRepository;
import com.newlocal.repository.search.StockSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.List;
import java.lang.Object;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private StockRepository stockRepository;

    private StockSearchRepository stockSearchRepository;

    public StockService(StockRepository stockRepository, StockSearchRepository stockSearchRepository) {
        this.stockRepository = stockRepository;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Save a stock.
     *
     * @param stock the entity to save
     * @return the persisted entity
     */
    public Stock save(Stock stock) {
        log.debug("Request to save Stock : {}", stock);
        Stock result = stockRepository.save(stock);
        stockSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable);
    }


    /**
     * Get one stock by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Stock> findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        return stockRepository.findById(id);
    }

    /**
     * Delete the stock by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.deleteById(id);
        stockSearchRepository.deleteById(id);
    }

    /**
     * Search for the stock corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stocks for query {}", query);
        return stockSearchRepository.search(queryStringQuery(query), pageable);    }



    /**
     * Search for the stock bio
     *
     */
    @Transactional(readOnly = true)
    public List<Stock> getProductBio() {
        log.debug("Request to search for Product Bio {}");
        return stockRepository.getProductBio();
    }


     /**
     * Search last new stock
     *
     */
     
    @Transactional(readOnly = true)
    public List<Stock> getNewStock() {
        log.debug("Request to search a new stock {}");
        List <Stock> ListOrder=stockRepository.findAllStocks(new Sort(Sort.Direction.DESC, "onSaleDate"));
        return ListOrder;
    }

         /**
     * Search the best purchase
     *
     */
     
    @Transactional(readOnly = true)
    public List<Stock> getBestPurchase() {
        log.debug("Request to search the best purchase {}");
        return stockRepository.getBestPurchase();
   
    }

        /**
     * Search the best grade
     *
     */
     
    @Transactional(readOnly = true)
    public List<Stock> getBestGrade() {
        log.debug("Request to search the best grade {}");
        return stockRepository.getBestGrade();
   
    }
}
