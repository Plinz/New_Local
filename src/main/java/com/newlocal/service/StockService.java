package com.newlocal.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newlocal.domain.Stock;
import com.newlocal.repository.StockRepository;
import com.newlocal.repository.search.StockSearchRepository;
import com.newlocal.service.dto.StockDTO;
import com.newlocal.service.mapper.StockMapper;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private StockRepository stockRepository;

    private StockMapper stockMapper;

    private StockSearchRepository stockSearchRepository;

    public StockService(StockRepository stockRepository, StockMapper stockMapper, StockSearchRepository stockSearchRepository) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save
     * @return the persisted entity
     */
    public StockDTO save(StockDTO stockDTO) {
        log.debug("Request to save Stock : {}", stockDTO);
        Stock stock = stockMapper.toEntity(stockDTO);
        stock = stockRepository.save(stock);
        StockDTO result = stockMapper.toDto(stock);
        stockSearchRepository.save(stock);
        return result;
    }


    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable).map(StockDTO::new);
    }

    /**
     * Get one stock by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockDTO> findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        return stockRepository.findById(id).map(StockDTO::new);

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
    public Page<StockDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stocks for query {}", query);
        return stockSearchRepository.search(queryStringQuery(query), pageable).map(StockDTO::new);
    }

    /**
     * Search for the stock bio
     *
     */
    @Transactional(readOnly = true)
    public List<StockDTO> getProductBio() {
        log.debug("Request to search for Product Bio {}");
        return stockRepository.getProductBio().stream().map(StockDTO::new).collect(Collectors.toList());
    }

    /**
     * Search last new stock
     *
     */

    @Transactional(readOnly = true)
    public List<StockDTO> getNewStock() {
        log.debug("Request to search a new stock {}");
        return stockRepository.findAllStocks(new Sort(Sort.Direction.DESC, "onSaleDate"))
        		.stream().map(StockDTO::new).collect(Collectors.toList());
    }

    /**
     * Search the best purchase
     *
     */

    @Transactional(readOnly = true)
    public List<StockDTO> getBestPurchase() {
        log.debug("Request to search the best purchase {}");
        return stockRepository.getBestPurchase()
        		.stream().map(StockDTO::new).collect(Collectors.toList());
    }

    /**
     * Search the best grade
     *
     */

    @Transactional(readOnly = true)
    public List<StockDTO> getBestGrade() {
        log.debug("Request to search the best grade {}");
        return stockRepository.getBestGrade()
        		.stream().map(StockDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockDTO> findBySellerIsCurrentUser() {
        return stockRepository.findBySellerIsCurrentUser()
        		.stream().map(StockDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getStockCat(String name) {
        return stockRepository.getStockCat(name)
        		.stream().map(StockDTO::new).collect(Collectors.toList());
    }

}
