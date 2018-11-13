package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.Holding;
import com.newlocal.domain.Location;
import com.newlocal.domain.User;
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
import com.newlocal.service.HoldingService;
import com.newlocal.web.rest.errors.ExceptionTranslator;
import com.newlocal.service.dto.HoldingCriteria;
import com.newlocal.service.HoldingQueryService;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the HoldingResource REST controller.
 *
 * @see HoldingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class HoldingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private HoldingRepository holdingRepository;
    
    @Autowired
    private HoldingService holdingService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.HoldingSearchRepositoryMockConfiguration
     */
    @Autowired
    private HoldingSearchRepository mockHoldingSearchRepository;

    @Autowired
    private HoldingQueryService holdingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHoldingMockMvc;

    private Holding holding;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HoldingResource holdingResource = new HoldingResource(holdingService, holdingQueryService);
        this.restHoldingMockMvc = MockMvcBuilders.standaloneSetup(holdingResource)
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
    public static Holding createEntity(EntityManager em) {
        Holding holding = new Holding()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return holding;
    }

    @Before
    public void initTest() {
        holding = createEntity(em);
    }

    @Test
    @Transactional
    public void createHolding() throws Exception {
        int databaseSizeBeforeCreate = holdingRepository.findAll().size();

        // Create the Holding
        restHoldingMockMvc.perform(post("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holding)))
            .andExpect(status().isCreated());

        // Validate the Holding in the database
        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeCreate + 1);
        Holding testHolding = holdingList.get(holdingList.size() - 1);
        assertThat(testHolding.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHolding.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHolding.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testHolding.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Holding in Elasticsearch
        verify(mockHoldingSearchRepository, times(1)).save(testHolding);
    }

    @Test
    @Transactional
    public void createHoldingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = holdingRepository.findAll().size();

        // Create the Holding with an existing ID
        holding.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoldingMockMvc.perform(post("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holding)))
            .andExpect(status().isBadRequest());

        // Validate the Holding in the database
        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeCreate);

        // Validate the Holding in Elasticsearch
        verify(mockHoldingSearchRepository, times(0)).save(holding);
    }

    @Test
    @Transactional
    public void getAllHoldings() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList
        restHoldingMockMvc.perform(get("/api/holdings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holding.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getHolding() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get the holding
        restHoldingMockMvc.perform(get("/api/holdings/{id}", holding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(holding.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getAllHoldingsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where name equals to DEFAULT_NAME
        defaultHoldingShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the holdingList where name equals to UPDATED_NAME
        defaultHoldingShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHoldingsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHoldingShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the holdingList where name equals to UPDATED_NAME
        defaultHoldingShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHoldingsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where name is not null
        defaultHoldingShouldBeFound("name.specified=true");

        // Get all the holdingList where name is null
        defaultHoldingShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllHoldingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where description equals to DEFAULT_DESCRIPTION
        defaultHoldingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the holdingList where description equals to UPDATED_DESCRIPTION
        defaultHoldingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHoldingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultHoldingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the holdingList where description equals to UPDATED_DESCRIPTION
        defaultHoldingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllHoldingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where description is not null
        defaultHoldingShouldBeFound("description.specified=true");

        // Get all the holdingList where description is null
        defaultHoldingShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllHoldingsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        holding.setLocation(location);
        holdingRepository.saveAndFlush(holding);
        Long locationId = location.getId();

        // Get all the holdingList where location equals to locationId
        defaultHoldingShouldBeFound("locationId.equals=" + locationId);

        // Get all the holdingList where location equals to locationId + 1
        defaultHoldingShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllHoldingsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        holding.setOwner(owner);
        holdingRepository.saveAndFlush(holding);
        Long ownerId = owner.getId();

        // Get all the holdingList where owner equals to ownerId
        defaultHoldingShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the holdingList where owner equals to ownerId + 1
        defaultHoldingShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHoldingShouldBeFound(String filter) throws Exception {
        restHoldingMockMvc.perform(get("/api/holdings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holding.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restHoldingMockMvc.perform(get("/api/holdings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHoldingShouldNotBeFound(String filter) throws Exception {
        restHoldingMockMvc.perform(get("/api/holdings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHoldingMockMvc.perform(get("/api/holdings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHolding() throws Exception {
        // Get the holding
        restHoldingMockMvc.perform(get("/api/holdings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHolding() throws Exception {
        // Initialize the database
        holdingService.save(holding);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHoldingSearchRepository);

        int databaseSizeBeforeUpdate = holdingRepository.findAll().size();

        // Update the holding
        Holding updatedHolding = holdingRepository.findById(holding.getId()).get();
        // Disconnect from session so that the updates on updatedHolding are not directly saved in db
        em.detach(updatedHolding);
        updatedHolding
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restHoldingMockMvc.perform(put("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHolding)))
            .andExpect(status().isOk());

        // Validate the Holding in the database
        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeUpdate);
        Holding testHolding = holdingList.get(holdingList.size() - 1);
        assertThat(testHolding.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHolding.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHolding.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testHolding.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Holding in Elasticsearch
        verify(mockHoldingSearchRepository, times(1)).save(testHolding);
    }

    @Test
    @Transactional
    public void updateNonExistingHolding() throws Exception {
        int databaseSizeBeforeUpdate = holdingRepository.findAll().size();

        // Create the Holding

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldingMockMvc.perform(put("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holding)))
            .andExpect(status().isBadRequest());

        // Validate the Holding in the database
        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holding in Elasticsearch
        verify(mockHoldingSearchRepository, times(0)).save(holding);
    }

    @Test
    @Transactional
    public void deleteHolding() throws Exception {
        // Initialize the database
        holdingService.save(holding);

        int databaseSizeBeforeDelete = holdingRepository.findAll().size();

        // Get the holding
        restHoldingMockMvc.perform(delete("/api/holdings/{id}", holding.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Holding in Elasticsearch
        verify(mockHoldingSearchRepository, times(1)).deleteById(holding.getId());
    }

    @Test
    @Transactional
    public void searchHolding() throws Exception {
        // Initialize the database
        holdingService.save(holding);
        when(mockHoldingSearchRepository.search(queryStringQuery("id:" + holding.getId())))
            .thenReturn(Collections.singletonList(holding));
        // Search the holding
        restHoldingMockMvc.perform(get("/api/_search/holdings?query=id:" + holding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holding.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Holding.class);
        Holding holding1 = new Holding();
        holding1.setId(1L);
        Holding holding2 = new Holding();
        holding2.setId(holding1.getId());
        assertThat(holding1).isEqualTo(holding2);
        holding2.setId(2L);
        assertThat(holding1).isNotEqualTo(holding2);
        holding1.setId(null);
        assertThat(holding1).isNotEqualTo(holding2);
    }
}
