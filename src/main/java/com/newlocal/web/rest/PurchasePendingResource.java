package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.PurchasePending;
import com.newlocal.repository.PurchasePendingRepository;
import com.newlocal.repository.search.PurchasePendingSearchRepository;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PurchasePending.
 */
@RestController
@RequestMapping("/api")
public class PurchasePendingResource {

    private final Logger log = LoggerFactory.getLogger(PurchasePendingResource.class);

    private static final String ENTITY_NAME = "purchasePending";

    private PurchasePendingRepository purchasePendingRepository;

    private PurchasePendingSearchRepository purchasePendingSearchRepository;

    public PurchasePendingResource(PurchasePendingRepository purchasePendingRepository, PurchasePendingSearchRepository purchasePendingSearchRepository) {
        this.purchasePendingRepository = purchasePendingRepository;
        this.purchasePendingSearchRepository = purchasePendingSearchRepository;
    }

    /**
     * POST  /purchase-pendings : Create a new purchasePending.
     *
     * @param purchasePending the purchasePending to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchasePending, or with status 400 (Bad Request) if the purchasePending has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-pendings")
    @Timed
    public ResponseEntity<PurchasePending> createPurchasePending(@Valid @RequestBody PurchasePending purchasePending) throws URISyntaxException {
        log.debug("REST request to save PurchasePending : {}", purchasePending);
        if (purchasePending.getId() != null) {
            throw new BadRequestAlertException("A new purchasePending cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchasePending result = purchasePendingRepository.save(purchasePending);
        purchasePendingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/purchase-pendings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-pendings : Updates an existing purchasePending.
     *
     * @param purchasePending the purchasePending to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchasePending,
     * or with status 400 (Bad Request) if the purchasePending is not valid,
     * or with status 500 (Internal Server Error) if the purchasePending couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-pendings")
    @Timed
    public ResponseEntity<PurchasePending> updatePurchasePending(@Valid @RequestBody PurchasePending purchasePending) throws URISyntaxException {
        log.debug("REST request to update PurchasePending : {}", purchasePending);
        if (purchasePending.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchasePending result = purchasePendingRepository.save(purchasePending);
        purchasePendingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchasePending.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-pendings : get all the purchasePendings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of purchasePendings in body
     */
    @GetMapping("/purchase-pendings")
    @Timed
    public List<PurchasePending> getAllPurchasePendings() {
        log.debug("REST request to get all PurchasePendings");
        return purchasePendingRepository.findAll();
    }

    /**
     * GET  /purchase-pendings/:id : get the "id" purchasePending.
     *
     * @param id the id of the purchasePending to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchasePending, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-pendings/{id}")
    @Timed
    public ResponseEntity<PurchasePending> getPurchasePending(@PathVariable Long id) {
        log.debug("REST request to get PurchasePending : {}", id);
        Optional<PurchasePending> purchasePending = purchasePendingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(purchasePending);
    }

    /**
     * DELETE  /purchase-pendings/:id : delete the "id" purchasePending.
     *
     * @param id the id of the purchasePending to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-pendings/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchasePending(@PathVariable Long id) {
        log.debug("REST request to delete PurchasePending : {}", id);

        purchasePendingRepository.deleteById(id);
        purchasePendingSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purchase-pendings?query=:query : search for the purchasePending corresponding
     * to the query.
     *
     * @param query the query of the purchasePending search
     * @return the result of the search
     */
    @GetMapping("/_search/purchase-pendings")
    @Timed
    public List<PurchasePending> searchPurchasePendings(@RequestParam String query) {
        log.debug("REST request to search PurchasePendings for query {}", query);
        return StreamSupport
            .stream(purchasePendingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
