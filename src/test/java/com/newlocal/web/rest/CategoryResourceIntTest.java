package com.newlocal.web.rest;

import static com.newlocal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.newlocal.NewLocalApp;
import com.newlocal.domain.Category;
import com.newlocal.domain.Image;
import com.newlocal.repository.CategoryRepository;
import com.newlocal.repository.search.CategorySearchRepository;
import com.newlocal.service.CategoryQueryService;
import com.newlocal.service.CategoryService;
import com.newlocal.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CategoryResource REST controller.
 *
 * @see CategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class CategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.CategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private CategorySearchRepository mockCategorySearchRepository;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCategoryMockMvc;

    private Category category;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoryResource categoryResource = new CategoryResource(categoryService, categoryQueryService);
        this.restCategoryMockMvc = MockMvcBuilders.standaloneSetup(categoryResource)
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
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return category;
    }

    @Before
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category
        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Category in Elasticsearch
        verify(mockCategorySearchRepository, times(1)).save(testCategory);
    }

    @Test
    @Transactional
    public void createCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category with an existing ID
        category.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the Category in Elasticsearch
        verify(mockCategorySearchRepository, times(0)).save(category);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setName(null);

        // Create the Category, which fails.

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name equals to DEFAULT_NAME
        defaultCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name is not null
        defaultCategoryShouldBeFound("name.specified=true");

        // Get all the categoryList where name is null
        defaultCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description equals to DEFAULT_DESCRIPTION
        defaultCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description equals to UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the categoryList where description equals to UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description is not null
        defaultCategoryShouldBeFound("description.specified=true");

        // Get all the categoryList where description is null
        defaultCategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        Image image = ImageResourceIntTest.createEntity(em);
        em.persist(image);
        em.flush();
        category.setImage(image);
        categoryRepository.saveAndFlush(category);
        Long imageId = image.getId();

        // Get all the categoryList where image equals to imageId
        defaultCategoryShouldBeFound("imageId.equals=" + imageId);

        // Get all the categoryList where image equals to imageId + 1
        defaultCategoryShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }


    @Test
    @Transactional
    public void getAllCategoriesByCategoryParentIsEqualToSomething() throws Exception {
        // Initialize the database
        Category categoryParent = CategoryResourceIntTest.createEntity(em);
        em.persist(categoryParent);
        em.flush();
        category.setCategoryParent(categoryParent);
        categoryRepository.saveAndFlush(category);
        Long categoryParentId = categoryParent.getId();

        // Get all the categoryList where categoryParent equals to categoryParentId
        defaultCategoryShouldBeFound("categoryParentId.equals=" + categoryParentId);

        // Get all the categoryList where categoryParent equals to categoryParentId + 1
        defaultCategoryShouldNotBeFound("categoryParentId.equals=" + (categoryParentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCategoryShouldBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCategoryShouldNotBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategory() throws Exception {
        // Initialize the database
        categoryService.save(category);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCategorySearchRepository);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restCategoryMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategory)))
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Category in Elasticsearch
        verify(mockCategorySearchRepository, times(1)).save(testCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Create the Category

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(category)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Category in Elasticsearch
        verify(mockCategorySearchRepository, times(0)).save(category);
    }

    @Test
    @Transactional
    public void deleteCategory() throws Exception {
        // Initialize the database
        categoryService.save(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Get the category
        restCategoryMockMvc.perform(delete("/api/categories/{id}", category.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Category in Elasticsearch
        verify(mockCategorySearchRepository, times(1)).deleteById(category.getId());
    }

    @Test
    @Transactional
    public void searchCategory() throws Exception {
        // Initialize the database
        categoryService.save(category);
        when(mockCategorySearchRepository.search(queryStringQuery("id:" + category.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(category), PageRequest.of(0, 1), 1));
        // Search the category
        restCategoryMockMvc.perform(get("/api/_search/categories?query=id:" + category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);
        category2.setId(2L);
        assertThat(category1).isNotEqualTo(category2);
        category1.setId(null);
        assertThat(category1).isNotEqualTo(category2);
    }
}
