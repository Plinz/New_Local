package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.PurchaseDone;
import com.newlocal.service.PurchaseDoneService;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.service.dto.PurchaseDoneCriteria;
import com.newlocal.service.PurchaseDoneQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PurchaseDone.
 */
@RestController
@RequestMapping("/api")
public class PurchaseDoneResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseDoneResource.class);

    private static final String ENTITY_NAME = "purchaseDone";

    private PurchaseDoneService purchaseDoneService;

    private PurchaseDoneQueryService purchaseDoneQueryService;

    public PurchaseDoneResource(PurchaseDoneService purchaseDoneService, PurchaseDoneQueryService purchaseDoneQueryService) {
        this.purchaseDoneService = purchaseDoneService;
        this.purchaseDoneQueryService = purchaseDoneQueryService;
    }

    /**
     * POST  /purchase-dones : Create a new purchaseDone.
     *
     * @param purchaseDone the purchaseDone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseDone, or with status 400 (Bad Request) if the purchaseDone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-dones")
    @Timed
    public ResponseEntity<PurchaseDone> createPurchaseDone(@Valid @RequestBody PurchaseDone purchaseDone) throws URISyntaxException {
        log.debug("REST request to save PurchaseDone : {}", purchaseDone);
        if (purchaseDone.getId() != null) {
            throw new BadRequestAlertException("A new purchaseDone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseDone result = purchaseDoneService.save(purchaseDone);
        return ResponseEntity.created(new URI("/api/purchase-dones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-dones : Updates an existing purchaseDone.
     *
     * @param purchaseDone the purchaseDone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseDone,
     * or with status 400 (Bad Request) if the purchaseDone is not valid,
     * or with status 500 (Internal Server Error) if the purchaseDone couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-dones")
    @Timed
    public ResponseEntity<PurchaseDone> updatePurchaseDone(@Valid @RequestBody PurchaseDone purchaseDone) throws URISyntaxException {
        log.debug("REST request to update PurchaseDone : {}", purchaseDone);
        if (purchaseDone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseDone result = purchaseDoneService.save(purchaseDone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseDone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-dones : get all the purchaseDones.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseDones in body
     */
    @GetMapping("/purchase-dones")
    @Timed
    public ResponseEntity<List<PurchaseDone>> getAllPurchaseDones(PurchaseDoneCriteria criteria) {
        log.debug("REST request to get PurchaseDones by criteria: {}", criteria);
        List<PurchaseDone> entityList = purchaseDoneQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /purchase-dones/count : count all the purchaseDones.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/purchase-dones/count")
    @Timed
    public ResponseEntity<Long> countPurchaseDones(PurchaseDoneCriteria criteria) {
        log.debug("REST request to count PurchaseDones by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseDoneQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /purchase-dones/:id : get the "id" purchaseDone.
     *
     * @param id the id of the purchaseDone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseDone, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-dones/{id}")
    @Timed
    public ResponseEntity<PurchaseDone> getPurchaseDone(@PathVariable Long id) {
        log.debug("REST request to get PurchaseDone : {}", id);
        Optional<PurchaseDone> purchaseDone = purchaseDoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseDone);
    }

    /**
     * DELETE  /purchase-dones/:id : delete the "id" purchaseDone.
     *
     * @param id the id of the purchaseDone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-dones/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchaseDone(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseDone : {}", id);
        purchaseDoneService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purchase-dones?query=:query : search for the purchaseDone corresponding
     * to the query.
     *
     * @param query the query of the purchaseDone search
     * @return the result of the search
     */
    @GetMapping("/_search/purchase-dones")
    @Timed
    public List<PurchaseDone> searchPurchaseDones(@RequestParam String query) {
        log.debug("REST request to search PurchaseDones for query {}", query);
        return purchaseDoneService.search(query);
    }

}
