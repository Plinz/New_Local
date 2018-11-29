package com.newlocal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.newlocal.domain.Grade;
import com.newlocal.service.GradeService;
import com.newlocal.web.rest.errors.BadRequestAlertException;
import com.newlocal.web.rest.util.HeaderUtil;
import com.newlocal.web.rest.util.PaginationUtil;
import com.newlocal.service.dto.GradeCriteria;
import com.newlocal.service.GradeQueryService;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Grade.
 */
@RestController
@RequestMapping("/api")
public class GradeResource {

    private final Logger log = LoggerFactory.getLogger(GradeResource.class);

    private static final String ENTITY_NAME = "grade";

    private GradeService gradeService;

    private GradeQueryService gradeQueryService;

    public GradeResource(GradeService gradeService, GradeQueryService gradeQueryService) {
        this.gradeService = gradeService;
        this.gradeQueryService = gradeQueryService;
    }

    /**
     * POST  /grades : Create a new grade.
     *
     * @param grade the grade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new grade, or with status 400 (Bad Request) if the grade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/grades")
    @Timed
    public ResponseEntity<Grade> createGrade(@Valid @RequestBody Grade grade) throws URISyntaxException {
        log.debug("REST request to save Grade : {}", grade);
        if (grade.getId() != null) {
            throw new BadRequestAlertException("A new grade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Grade result = gradeService.save(grade);
        return ResponseEntity.created(new URI("/api/grades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /grades : Updates an existing grade.
     *
     * @param grade the grade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated grade,
     * or with status 400 (Bad Request) if the grade is not valid,
     * or with status 500 (Internal Server Error) if the grade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/grades")
    @Timed
    public ResponseEntity<Grade> updateGrade(@Valid @RequestBody Grade grade) throws URISyntaxException {
        log.debug("REST request to update Grade : {}", grade);
        if (grade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Grade result = gradeService.save(grade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, grade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /grades : get all the grades.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of grades in body
     */
    @GetMapping("/grades")
    @Timed
    public ResponseEntity<List<Grade>> getAllGrades(GradeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Grades by criteria: {}", criteria);
        Page<Grade> page = gradeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/grades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
    * GET  /grades/count : count all the grades.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/grades/count")
    @Timed
    public ResponseEntity<Long> countGrades(GradeCriteria criteria) {
        log.debug("REST request to count Grades by criteria: {}", criteria);
        return ResponseEntity.ok().body(gradeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /grades/:id : get the "id" grade.
     *
     * @param id the id of the grade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the grade, or with status 404 (Not Found)
     */
    @GetMapping("/grades/{id}")
    @Timed
    public ResponseEntity<Grade> getGrade(@PathVariable Long id) {
        log.debug("REST request to get Grade : {}", id);
        Optional<Grade> grade = gradeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(grade);
    }

    /**
     * DELETE  /grades/:id : delete the "id" grade.
     *
     * @param id the id of the grade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/grades/{id}")
    @Timed
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        log.debug("REST request to delete Grade : {}", id);
        gradeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/grades?query=:query : search for the grade corresponding
     * to the query.
     *
     * @param query the query of the grade search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/grades")
    @Timed
    public ResponseEntity<List<Grade>> searchGrades(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Grades for query {}", query);
        Page<Grade> page = gradeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/grades");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
