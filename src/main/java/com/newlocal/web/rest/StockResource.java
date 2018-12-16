package com.newlocal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.service.StockQueryService;
import com.newlocal.service.StockService;
import com.newlocal.service.dto.StockCriteria;
import com.newlocal.service.dto.StockDTO;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import com.newlocal.service.dto.UserDTO;

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


    private UserDAO userDAO;

    public StockResource(StockService stockService, StockQueryService stockQueryService, UserDAO userDAO) {
        this.stockService = stockService;
        this.stockQueryService = stockQueryService;
        this.userDAO = userDAO;
    }

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stockDTO the stockDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockDTO, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stocks")
    @Timed
    public ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stockDTO);
        if (stockDTO.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        long idCurrentUser = userDAO.getUserIdByCurrentLogin();
        if (idCurrentUser > 0) {
            stockDTO.setSellerId(idCurrentUser);
        } else {
            stockDTO.setSellerId(null);
        }
        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stockDTO the stockDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockDTO,
     * or with status 400 (Bad Request) if the stockDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocks")
    @Timed
    public ResponseEntity<StockDTO> updateStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stockDTO);
        if (stockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockDTO.getId().toString()))
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
    public ResponseEntity<List<StockDTO>> getAllStocks(StockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stocks by criteria: {}", criteria);
        Page<StockDTO> page = stockQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the stockDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<StockDTO> stockDTO = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockDTO);
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stockDTO to delete
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
    public ResponseEntity<List<StockDTO>> searchStocks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Stocks for query {}", query);
        Page<StockDTO> page = stockService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stocks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH Random Product Bio
     *
     */
    @GetMapping("/stocks/bio")
    @Timed
    public ResponseEntity<StockDTO>  getProductBio() {
        log.debug("REST request to search a product Bio");
        List<StockDTO>  stockBio = stockService.getProductBio();
        StockDTO stockb = stockBio.get((new Random()).nextInt(stockBio.size()));
        return ResponseEntity.ok().body(stockb);
    }

    /**
     * SEARCH last New Stock
     *
     */
    @GetMapping("/stocks/newStock")
    @Timed
    public ResponseEntity<StockDTO>  getNewStock() {
        log.debug("REST request to search a new stock");
        StockDTO stockNew = stockService.getNewStock().get(0);
        return ResponseEntity.ok().body(stockNew);

    }

    /**
     * SEARCH the best purchase
     *
     */
    @GetMapping("/stocks/bestPurchase")
    @Timed
    public ResponseEntity<StockDTO> getBestPurchase() {
        log.debug("REST request to search the best purchase");
        List<StockDTO> stockBestPurchase = stockService.getBestPurchase();
        StockDTO stockBest = stockBestPurchase.get((new Random()).nextInt(stockBestPurchase.size()));
        return ResponseEntity.ok().body(stockBest);
    }

    /**
     * SEARCH the best purchase
     *
     */

    @GetMapping("/stocks/grade")
    @Timed
    public ResponseEntity<StockDTO> getBestGrade() {
        log.debug("REST request to search the best grade");
        List<StockDTO> gradeList = stockService.getBestGrade();
        StockDTO stockGrade = gradeList.get((new Random()).nextInt(gradeList.size()));
        return ResponseEntity.ok().body(stockGrade);
    }

    @GetMapping("/stocks/currentuser")
    @Timed
    public ResponseEntity<List<StockDTO>> findBySellerIsCurrentUser() {
        List<StockDTO> stocks = stockService.findBySellerIsCurrentUser();
        return new ResponseEntity<List<StockDTO>>(stocks, HttpStatus.OK);

    }

    @GetMapping("/stocks/category/{name}")
    @Timed
    public ResponseEntity<List<StockDTO>> getStockCat(@PathVariable String name) {
        List<StockDTO> stocks = stockService.getStockCat(name);
        return new ResponseEntity<List<StockDTO>>(stocks, HttpStatus.OK);
    }

    @GetMapping("/stocks/filter/{cat}/{seller}/{min}/{max}")
    @Timed
    public ResponseEntity<List<StockDTO>> filterMainsearch(@PathVariable String cat,@PathVariable String seller,@PathVariable Long min,@PathVariable Long max) {
        List<StockDTO> stocks = null;

        // if (cat != "null" && seller !="null"){
         stocks = stockService.filterCatSeller(cat,seller,min,max);
        // }else if(cat != "null"){
        //     stocks = stockService.filterCat(cat,min,max);
        // }else if(seller != "null"){
        //     stocks = stockService.filterSeller(seller,min,max);
        // }else{
        //     stocks = stockService.filterPrice(min,max);
        // }
        return new ResponseEntity<List<StockDTO>>(stocks, HttpStatus.OK);
    }

    @GetMapping("/stocks/allseller")
    @Timed
    public ResponseEntity<List<UserDTO>> allSeller() {
        List<UserDTO> user = stockService.allSeller();
        return new ResponseEntity<List<UserDTO>>(user, HttpStatus.OK);
    }
}
