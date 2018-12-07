package com.newlocal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
import com.newlocal.domain.Purchase;
import com.newlocal.service.PurchaseQueryService;
import com.newlocal.service.PurchaseService;
import com.newlocal.service.dto.PurchaseCriteria;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import com.newlocal.service.CartService;
import com.newlocal.domain.Cart;

/**
 * REST controller for managing Purchase.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    private static final String ENTITY_NAME = "purchase";

    private PurchaseService purchaseService;

    private PurchaseQueryService purchaseQueryService;
    
    private CartService cartService;

    public PurchaseResource(PurchaseService purchaseService, PurchaseQueryService purchaseQueryService, CartService cartService) {
        this.purchaseService = purchaseService;
        this.purchaseQueryService = purchaseQueryService;
        this.cartService = cartService;
    }

    /**
     * POST  /purchases : Create a new purchase.
     *
     * @param purchase the purchase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchase, or with status 400 (Bad Request) if the purchase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchases")
    @Timed
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchase);
        if (purchase.getId() != null) {
            throw new BadRequestAlertException("A new purchase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Purchase result = purchaseService.save(purchase);
        return ResponseEntity.created(new URI("/api/purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchases : Updates an existing purchase.
     *
     * @param purchase the purchase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchase,
     * or with status 400 (Bad Request) if the purchase is not valid,
     * or with status 500 (Internal Server Error) if the purchase couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchases")
    @Timed
    public ResponseEntity<Purchase> updatePurchase(@Valid @RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}", purchase);
        if (purchase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Purchase result = purchaseService.save(purchase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchases : get all the purchases.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of purchases in body
     */
    @GetMapping("/purchases")
    @Timed
    public ResponseEntity<List<Purchase>> getAllPurchases(PurchaseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Purchases by criteria: {}", criteria);
        Page<Purchase> page = purchaseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /purchases/count : count all the purchases.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/purchases/count")
    @Timed
    public ResponseEntity<Long> countPurchases(PurchaseCriteria criteria) {
        log.debug("REST request to count Purchases by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /purchases/:id : get the "id" purchase.
     *
     * @param id the id of the purchase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchase, or with status 404 (Not Found)
     */
    @GetMapping("/purchases/{id}")
    @Timed
    public ResponseEntity<Purchase> getPurchase(@PathVariable Long id) {
        log.debug("REST request to get Purchase : {}", id);
        Optional<Purchase> purchase = purchaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchase);
    }

    /**
     * DELETE  /purchases/:id : delete the "id" purchase.
     *
     * @param id the id of the purchase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchases/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        log.debug("REST request to delete Purchase : {}", id);
        purchaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purchases?query=:query : search for the purchase corresponding
     * to the query.
     *
     * @param query the query of the purchase search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/purchases")
    @Timed
    public ResponseEntity<List<Purchase>> searchPurchases(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Purchases for query {}", query);
        Page<Purchase> page = purchaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/purchases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/purchases/stock/{id}")
    @Timed
    public ResponseEntity<List<Purchase>> getPStock(@PathVariable Long id) {
        List<Purchase> purchase = purchaseService.getPStock(id);
        return new ResponseEntity<List<Purchase>>(purchase, HttpStatus.OK);
        //return ResponseEntity.ok().body(purchase);
    }

    @DeleteMapping("/purchases/updatedata/mail/{id}")
    @Timed
    public String sendMail(@PathVariable Long id) {
    	
        //Recupération des valeurs
    	List<Cart> carts = cartService.getCardUser(id);
    	
    	//Creation de la bdd / suppression
    	for (Cart c : carts) {
    		//Purchase p = new Purchase(c);
    		//p.setId(null);
            //purchaseService.save(p);
        	//Suppression des carts
            cartService.delete(c.getId());
		}

    	// Mettre l'envoie de mail
 		//ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    	return carts.toString();
    }
    
}
