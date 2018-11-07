package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.PurchaseDone;
import com.newlocal.repository.PurchaseDoneRepository;
import com.newlocal.repository.search.PurchaseDoneSearchRepository;
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
 * REST controller for managing PurchaseDone.
 */
@RestController
@RequestMapping("/api")
public class PurchaseDoneResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseDoneResource.class);

    private static final String ENTITY_NAME = "purchaseDone";

    private PurchaseDoneRepository purchaseDoneRepository;

    private PurchaseDoneSearchRepository purchaseDoneSearchRepository;

    public PurchaseDoneResource(PurchaseDoneRepository purchaseDoneRepository, PurchaseDoneSearchRepository purchaseDoneSearchRepository) {
        this.purchaseDoneRepository = purchaseDoneRepository;
        this.purchaseDoneSearchRepository = purchaseDoneSearchRepository;
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
        PurchaseDone result = purchaseDoneRepository.save(purchaseDone);
        purchaseDoneSearchRepository.save(result);
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
        PurchaseDone result = purchaseDoneRepository.save(purchaseDone);
        purchaseDoneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseDone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-dones : get all the purchaseDones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseDones in body
     */
    @GetMapping("/purchase-dones")
    @Timed
    public List<PurchaseDone> getAllPurchaseDones() {
        log.debug("REST request to get all PurchaseDones");
        return purchaseDoneRepository.findAll();
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
        Optional<PurchaseDone> purchaseDone = purchaseDoneRepository.findById(id);
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

        purchaseDoneRepository.deleteById(id);
        purchaseDoneSearchRepository.deleteById(id);
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
        return StreamSupport
            .stream(purchaseDoneSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
