package com.newlocal.service;

import com.newlocal.domain.Grade;
import com.newlocal.repository.GradeRepository;
import com.newlocal.repository.search.GradeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Grade.
 */
@Service
@Transactional
public class GradeService {

    private final Logger log = LoggerFactory.getLogger(GradeService.class);

    private GradeRepository gradeRepository;

    private GradeSearchRepository gradeSearchRepository;

    public GradeService(GradeRepository gradeRepository, GradeSearchRepository gradeSearchRepository) {
        this.gradeRepository = gradeRepository;
        this.gradeSearchRepository = gradeSearchRepository;
    }

    /**
     * Save a grade.
     *
     * @param grade the entity to save
     * @return the persisted entity
     */
    public Grade save(Grade grade) {
        log.debug("Request to save Grade : {}", grade);
        Grade result = gradeRepository.save(grade);
        gradeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the grades.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Grade> findAll() {
        log.debug("Request to get all Grades");
        return gradeRepository.findAll();
    }


    /**
     * Get one grade by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Grade> findOne(Long id) {
        log.debug("Request to get Grade : {}", id);
        return gradeRepository.findById(id);
    }

    /**
     * Delete the grade by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Grade : {}", id);
        gradeRepository.deleteById(id);
        gradeSearchRepository.deleteById(id);
    }

    /**
     * Search for the grade corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Grade> search(String query) {
        log.debug("Request to search Grades for query {}", query);
        return StreamSupport
            .stream(gradeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
