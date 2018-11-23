package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.Holding;
import com.newlocal.service.HoldingService;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.service.dto.HoldingCriteria;
import com.newlocal.service.HoldingQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Holding.
 */
@RestController
@RequestMapping("/api")
public class HoldingResource {

    private final Logger log = LoggerFactory.getLogger(HoldingResource.class);

    private static final String ENTITY_NAME = "holding";

    private HoldingService holdingService;

    private HoldingQueryService holdingQueryService;

    public HoldingResource(HoldingService holdingService, HoldingQueryService holdingQueryService) {
        this.holdingService = holdingService;
        this.holdingQueryService = holdingQueryService;
    }

    /**
     * POST  /holdings : Create a new holding.
     *
     * @param holding the holding to create
     * @return the ResponseEntity with status 201 (Created) and with body the new holding, or with status 400 (Bad Request) if the holding has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/holdings")
    @Timed
    public ResponseEntity<Holding> createHolding(@RequestBody Holding holding) throws URISyntaxException {
        log.debug("REST request to save Holding : {}", holding);
        if (holding.getId() != null) {
            throw new BadRequestAlertException("A new holding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Holding result = holdingService.save(holding);
        return ResponseEntity.created(new URI("/api/holdings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /holdings : Updates an existing holding.
     *
     * @param holding the holding to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated holding,
     * or with status 400 (Bad Request) if the holding is not valid,
     * or with status 500 (Internal Server Error) if the holding couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/holdings")
    @Timed
    public ResponseEntity<Holding> updateHolding(@RequestBody Holding holding) throws URISyntaxException {
        log.debug("REST request to update Holding : {}", holding);
        if (holding.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Holding result = holdingService.save(holding);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, holding.getId().toString()))
            .body(result);
    }

    /**
     * GET  /holdings : get all the holdings.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of holdings in body
     */
    @GetMapping("/holdings")
    @Timed
    public ResponseEntity<List<Holding>> getAllHoldings(HoldingCriteria criteria) {
        log.debug("REST request to get Holdings by criteria: {}", criteria);
        List<Holding> entityList = holdingQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * GET  /holdings/count : count all the holdings.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/holdings/count")
    @Timed
    public ResponseEntity<Long> countHoldings(HoldingCriteria criteria) {
        log.debug("REST request to count Holdings by criteria: {}", criteria);
        return ResponseEntity.ok().body(holdingQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /holdings/:id : get the "id" holding.
     *
     * @param id the id of the holding to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the holding, or with status 404 (Not Found)
     */
    @GetMapping("/holdings/{id}")
    @Timed
    public ResponseEntity<Holding> getHolding(@PathVariable Long id) {
        log.debug("REST request to get Holding : {}", id);
        Optional<Holding> holding = holdingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holding);
    }

    /**
     * GET /holdings/currentUser : get the current user holdings
     *
     * @return the ResponseEntity with status 200 (OK) and with body the holdings, or with status 404 (Not Found)
     */
    @GetMapping("/holdings/currentUser")
    @Timed
    public ResponseEntity<List<Holding>> getHoldingsByCurrentUser(){
        log.debug("REST request to get Holdings of the current user : {}");
        List<Holding> holdings = holdingService.findByCurrentUser();
        return ResponseEntity.ok().body(holdings);
    }

    /**
     * DELETE  /holdings/:id : delete the "id" holding.
     *
     * @param id the id of the holding to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/holdings/{id}")
    @Timed
    public ResponseEntity<Void> deleteHolding(@PathVariable Long id) {
        log.debug("REST request to delete Holding : {}", id);
        holdingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/holdings?query=:query : search for the holding corresponding
     * to the query.
     *
     * @param query the query of the holding search
     * @return the result of the search
     */
    @GetMapping("/_search/holdings")
    @Timed
    public List<Holding> searchHoldings(@RequestParam String query) {
        log.debug("REST request to search Holdings for query {}", query);
        return holdingService.search(query);
    }
}
