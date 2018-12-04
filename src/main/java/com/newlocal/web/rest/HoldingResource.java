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
import com.newlocal.domain.Holding;
import com.newlocal.service.HoldingQueryService;
import com.newlocal.service.HoldingService;
import com.newlocal.service.dto.HoldingCriteria;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;
import com.newlocal.repository.UserRepository;

import io.github.jhipster.web.util.ResponseUtil;

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

    private UserRepository userRepository;

    private UserDAO userDAO;

    public HoldingResource(HoldingService holdingService, HoldingQueryService holdingQueryService, UserDAO userDAO, UserRepository userRepository) {
        this.holdingService = holdingService;
        this.holdingQueryService = holdingQueryService;
        this.userRepository = userRepository;
        this.userDAO = userDAO;
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
    public ResponseEntity<Holding> createHolding(@Valid @RequestBody Holding holding) throws URISyntaxException {
        log.debug("REST request to save Holding : {}", holding);
        if (holding.getId() != null) {
            throw new BadRequestAlertException("A new holding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        long idCurrentUser = userDAO.getUserIdByCurrentLogin();
        if (idCurrentUser > 0) {
            holding.setOwner(userRepository.findById(idCurrentUser).get());
        } else {
            holding.setOwner(null);
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
    public ResponseEntity<Holding> updateHolding(@Valid @RequestBody Holding holding) throws URISyntaxException {
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
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of holdings in body
     */
    @GetMapping("/holdings")
    @Timed
    public ResponseEntity<List<Holding>> getAllHoldings(HoldingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Holdings by criteria: {}", criteria);
        Page<Holding> page = holdingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/holdings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/holdings")
    @Timed
    public ResponseEntity<List<Holding>> searchHoldings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Holdings for query {}", query);
        Page<Holding> page = holdingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/holdings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
