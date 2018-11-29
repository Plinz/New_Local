package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.Stock;
import com.newlocal.service.StockService;
import com.newlocal.repository.UserRepository;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;
import com.newlocal.service.dto.StockCriteria;
import com.newlocal.service.StockQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import java.util.Random;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    private StockService stockService;

    private StockQueryService stockQueryService;

    private UserRepository userRepository;

    private UserDAO userDAO;

    public StockResource(StockService stockService, StockQueryService stockQueryService, UserRepository userRepository, UserDAO userDAO) {
        this.stockService = stockService;
        this.stockQueryService = stockQueryService;
        this.userRepository = userRepository;
        this.userDAO = userDAO;
    }

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stock the stock to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stock, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stocks")
    @Timed
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        long idCurrentUser = userDAO.getUserIdByCurrentLogin();
        if (idCurrentUser > 0) {
            stock.setSeller(userRepository.findById(idCurrentUser).get());
        } else {
            stock.setSeller(null);
        }
        Stock result = stockService.save(stock);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stock the stock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stock,
     * or with status 400 (Bad Request) if the stock is not valid,
     * or with status 500 (Internal Server Error) if the stock couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocks")
    @Timed
    public ResponseEntity<Stock> updateStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stock);
        if (stock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Stock result = stockService.save(stock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stock.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stocks : get all the stocks.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocks")
    @Timed
    public ResponseEntity<List<Stock>> getAllStocks(StockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stocks by criteria: {}", criteria);
        Page<Stock> page = stockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stocks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /stocks/count : count all the stocks.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/stocks/count")
    @Timed
    public ResponseEntity<Long> countStocks(StockCriteria criteria) {
        log.debug("REST request to count Stocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param id the id of the stock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stock, or with status 404 (Not Found)
     */
    @GetMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<Stock> stock = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stock);
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stock to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stocks?query=:query : search for the stock corresponding
     * to the query.
     *
     * @param query the query of the stock search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stocks")
    @Timed
    public ResponseEntity<List<Stock>> searchStocks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Stocks for query {}", query);
        Page<Stock> page = stockService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stocks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH Random Product Bio
     *
     */
    @GetMapping("/stocks/bio")
    @Timed
    public ResponseEntity<Stock>  getProductBio() {
        log.debug("REST request to search a product Bio");
        List<Stock>  stockBio= stockService.getProductBio();
        Stock stockb=stockBio.get((new Random()).nextInt(stockBio.size()));
        return ResponseEntity.ok().body(stockb);
    }

    /**
     * SEARCH last New Stock
     *
     */
    @GetMapping("/stocks/newStock")
    @Timed
    public ResponseEntity<Stock>  getNewStock() {
        log.debug("REST request to search a new stock");
        Stock stockNew=stockService.getNewStock().get(0);
        return ResponseEntity.ok().body(stockNew);

    }

    /**
     * SEARCH the best purchase
     *
     */
    @GetMapping("/stocks/bestPurchase")
    @Timed
    public ResponseEntity<Stock> getBestPurchase() {
        log.debug("REST request to search the best purchase");
        List<Stock>  stockBestPurchase=stockService.getBestPurchase();
        Stock stockBest=stockBestPurchase.get((new Random()).nextInt(stockBestPurchase.size()));
        return ResponseEntity.ok().body(stockBest);
    }

    /**
     * SEARCH the best purchase
     *
     */

    @GetMapping("/stocks/grade")
    @Timed
    public ResponseEntity<Stock> getBestGrade() {
        log.debug("REST request to search the best grade");
        List<Stock>  gradeList=stockService.getBestGrade();
        Stock stockGrade=gradeList.get((new Random()).nextInt(gradeList.size()));
        return ResponseEntity.ok().body(stockGrade);
    }
}
