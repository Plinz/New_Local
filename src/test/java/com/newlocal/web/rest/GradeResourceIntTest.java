package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.Grade;
import com.newlocal.domain.User;
import com.newlocal.domain.ProductType;
import com.newlocal.repository.GradeRepository;
import com.newlocal.repository.search.GradeSearchRepository;
import com.newlocal.service.GradeService;
import com.newlocal.web.rest.errors.ExceptionTranslator;
import com.newlocal.service.dto.GradeCriteria;
import com.newlocal.service.GradeQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.newlocal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GradeResource REST controller.
 *
 * @see GradeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class GradeResourceIntTest {

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;

    @Autowired
    private GradeRepository gradeRepository;
    
    @Autowired
    private GradeService gradeService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.GradeSearchRepositoryMockConfiguration
     */
    @Autowired
    private GradeSearchRepository mockGradeSearchRepository;

    @Autowired
    private GradeQueryService gradeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGradeMockMvc;

    private Grade grade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GradeResource gradeResource = new GradeResource(gradeService, gradeQueryService);
        this.restGradeMockMvc = MockMvcBuilders.standaloneSetup(gradeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grade createEntity(EntityManager em) {
        Grade grade = new Grade()
            .grade(DEFAULT_GRADE);
        return grade;
    }

    @Before
    public void initTest() {
        grade = createEntity(em);
    }

    @Test
    @Transactional
    public void createGrade() throws Exception {
        int databaseSizeBeforeCreate = gradeRepository.findAll().size();

        // Create the Grade
        restGradeMockMvc.perform(post("/api/grades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(grade)))
            .andExpect(status().isCreated());

        // Validate the Grade in the database
        List<Grade> gradeList = gradeRepository.findAll();
        assertThat(gradeList).hasSize(databaseSizeBeforeCreate + 1);
        Grade testGrade = gradeList.get(gradeList.size() - 1);
        assertThat(testGrade.getGrade()).isEqualTo(DEFAULT_GRADE);

        // Validate the Grade in Elasticsearch
        verify(mockGradeSearchRepository, times(1)).save(testGrade);
    }

    @Test
    @Transactional
    public void createGradeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gradeRepository.findAll().size();

        // Create the Grade with an existing ID
        grade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGradeMockMvc.perform(post("/api/grades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(grade)))
            .andExpect(status().isBadRequest());

        // Validate the Grade in the database
        List<Grade> gradeList = gradeRepository.findAll();
        assertThat(gradeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Grade in Elasticsearch
        verify(mockGradeSearchRepository, times(0)).save(grade);
    }

    @Test
    @Transactional
    public void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeRepository.findAll().size();
        // set the field null
        grade.setGrade(null);

        // Create the Grade, which fails.

        restGradeMockMvc.perform(post("/api/grades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(grade)))
            .andExpect(status().isBadRequest());

        List<Grade> gradeList = gradeRepository.findAll();
        assertThat(gradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGrades() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the gradeList
        restGradeMockMvc.perform(get("/api/grades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }
    
    @Test
    @Transactional
    public void getGrade() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get the grade
        restGradeMockMvc.perform(get("/api/grades/{id}", grade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(grade.getId().intValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE));
    }

    @Test
    @Transactional
    public void getAllGradesByGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the gradeList where grade equals to DEFAULT_GRADE
        defaultGradeShouldBeFound("grade.equals=" + DEFAULT_GRADE);

        // Get all the gradeList where grade equals to UPDATED_GRADE
        defaultGradeShouldNotBeFound("grade.equals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllGradesByGradeIsInShouldWork() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the gradeList where grade in DEFAULT_GRADE or UPDATED_GRADE
        defaultGradeShouldBeFound("grade.in=" + DEFAULT_GRADE + "," + UPDATED_GRADE);

        // Get all the gradeList where grade equals to UPDATED_GRADE
        defaultGradeShouldNotBeFound("grade.in=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllGradesByGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the gradeList where grade is not null
        defaultGradeShouldBeFound("grade.specified=true");

        // Get all the gradeList where grade is null
        defaultGradeShouldNotBeFound("grade.specified=false");
    }

    @Test
    @Transactional
    public void getAllGradesByGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the gradeList where grade greater than or equals to DEFAULT_GRADE
        defaultGradeShouldBeFound("grade.greaterOrEqualThan=" + DEFAULT_GRADE);

        // Get all the gradeList where grade greater than or equals to UPDATED_GRADE
        defaultGradeShouldNotBeFound("grade.greaterOrEqualThan=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllGradesByGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the gradeList where grade less than or equals to DEFAULT_GRADE
        defaultGradeShouldNotBeFound("grade.lessThan=" + DEFAULT_GRADE);

        // Get all the gradeList where grade less than or equals to UPDATED_GRADE
        defaultGradeShouldBeFound("grade.lessThan=" + UPDATED_GRADE);
    }


    @Test
    @Transactional
    public void getAllGradesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        grade.setUser(user);
        gradeRepository.saveAndFlush(grade);
        Long userId = user.getId();

        // Get all the gradeList where user equals to userId
        defaultGradeShouldBeFound("userId.equals=" + userId);

        // Get all the gradeList where user equals to userId + 1
        defaultGradeShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllGradesByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductType productType = ProductTypeResourceIntTest.createEntity(em);
        em.persist(productType);
        em.flush();
        grade.setProductType(productType);
        gradeRepository.saveAndFlush(grade);
        Long productTypeId = productType.getId();

        // Get all the gradeList where productType equals to productTypeId
        defaultGradeShouldBeFound("productTypeId.equals=" + productTypeId);

        // Get all the gradeList where productType equals to productTypeId + 1
        defaultGradeShouldNotBeFound("productTypeId.equals=" + (productTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultGradeShouldBeFound(String filter) throws Exception {
        restGradeMockMvc.perform(get("/api/grades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));

        // Check, that the count call also returns 1
        restGradeMockMvc.perform(get("/api/grades/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultGradeShouldNotBeFound(String filter) throws Exception {
        restGradeMockMvc.perform(get("/api/grades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGradeMockMvc.perform(get("/api/grades/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGrade() throws Exception {
        // Get the grade
        restGradeMockMvc.perform(get("/api/grades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGrade() throws Exception {
        // Initialize the database
        gradeService.save(grade);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockGradeSearchRepository);

        int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

        // Update the grade
        Grade updatedGrade = gradeRepository.findById(grade.getId()).get();
        // Disconnect from session so that the updates on updatedGrade are not directly saved in db
        em.detach(updatedGrade);
        updatedGrade
            .grade(UPDATED_GRADE);

        restGradeMockMvc.perform(put("/api/grades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGrade)))
            .andExpect(status().isOk());

        // Validate the Grade in the database
        List<Grade> gradeList = gradeRepository.findAll();
        assertThat(gradeList).hasSize(databaseSizeBeforeUpdate);
        Grade testGrade = gradeList.get(gradeList.size() - 1);
        assertThat(testGrade.getGrade()).isEqualTo(UPDATED_GRADE);

        // Validate the Grade in Elasticsearch
        verify(mockGradeSearchRepository, times(1)).save(testGrade);
    }

    @Test
    @Transactional
    public void updateNonExistingGrade() throws Exception {
        int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

        // Create the Grade

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradeMockMvc.perform(put("/api/grades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(grade)))
            .andExpect(status().isBadRequest());

        // Validate the Grade in the database
        List<Grade> gradeList = gradeRepository.findAll();
        assertThat(gradeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Grade in Elasticsearch
        verify(mockGradeSearchRepository, times(0)).save(grade);
    }

    @Test
    @Transactional
    public void deleteGrade() throws Exception {
        // Initialize the database
        gradeService.save(grade);

        int databaseSizeBeforeDelete = gradeRepository.findAll().size();

        // Get the grade
        restGradeMockMvc.perform(delete("/api/grades/{id}", grade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Grade> gradeList = gradeRepository.findAll();
        assertThat(gradeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Grade in Elasticsearch
        verify(mockGradeSearchRepository, times(1)).deleteById(grade.getId());
    }

    @Test
    @Transactional
    public void searchGrade() throws Exception {
        // Initialize the database
        gradeService.save(grade);
        when(mockGradeSearchRepository.search(queryStringQuery("id:" + grade.getId())))
            .thenReturn(Collections.singletonList(grade));
        // Search the grade
        restGradeMockMvc.perform(get("/api/_search/grades?query=id:" + grade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Grade.class);
        Grade grade1 = new Grade();
        grade1.setId(1L);
        Grade grade2 = new Grade();
        grade2.setId(grade1.getId());
        assertThat(grade1).isEqualTo(grade2);
        grade2.setId(2L);
        assertThat(grade1).isNotEqualTo(grade2);
        grade1.setId(null);
        assertThat(grade1).isNotEqualTo(grade2);
    }
}
