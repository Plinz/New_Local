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

import com.newlocal.repository.UserRepository;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.newlocal.NewLocalApp;
import com.newlocal.domain.Holding;
import com.newlocal.domain.Image;
import com.newlocal.domain.Location;
import com.newlocal.domain.User;
import com.newlocal.repository.HoldingRepository;
import com.newlocal.repository.search.HoldingSearchRepository;
import com.newlocal.service.HoldingQueryService;
import com.newlocal.service.HoldingService;
import com.newlocal.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the HoldingResource REST controller.
 *
 * @see HoldingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class HoldingResourceIntTest {

    private static final String DEFAULT_SIRET = "AAAAAAAAAAAAAA";
    private static final String UPDATED_SIRET = "BBBBBBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    @Autowired
    private UserRepository userRepository;

    private MockMvc restHoldingMockMvc;

    private Holding holding;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HoldingResource holdingResource = new HoldingResource(holdingService, holdingQueryService, userRepository, new UserDAO(new JdbcTemplate()));
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
            .siret(DEFAULT_SIRET)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        holding.setLocation(location);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        holding.setOwner(user);
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
        assertThat(testHolding.getSiret()).isEqualTo(DEFAULT_SIRET);
        assertThat(testHolding.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHolding.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

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
    public void checkSiretIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdingRepository.findAll().size();
        // set the field null
        holding.setSiret(null);

        // Create the Holding, which fails.

        restHoldingMockMvc.perform(post("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holding)))
            .andExpect(status().isBadRequest());

        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdingRepository.findAll().size();
        // set the field null
        holding.setName(null);

        // Create the Holding, which fails.

        restHoldingMockMvc.perform(post("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holding)))
            .andExpect(status().isBadRequest());

        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
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
            .andExpect(jsonPath("$.siret").value(DEFAULT_SIRET.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllHoldingsBySiretIsEqualToSomething() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where siret equals to DEFAULT_SIRET
        defaultHoldingShouldBeFound("siret.equals=" + DEFAULT_SIRET);

        // Get all the holdingList where siret equals to UPDATED_SIRET
        defaultHoldingShouldNotBeFound("siret.equals=" + UPDATED_SIRET);
    }

    @Test
    @Transactional
    public void getAllHoldingsBySiretIsInShouldWork() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where siret in DEFAULT_SIRET or UPDATED_SIRET
        defaultHoldingShouldBeFound("siret.in=" + DEFAULT_SIRET + "," + UPDATED_SIRET);

        // Get all the holdingList where siret equals to UPDATED_SIRET
        defaultHoldingShouldNotBeFound("siret.in=" + UPDATED_SIRET);
    }

    @Test
    @Transactional
    public void getAllHoldingsBySiretIsNullOrNotNull() throws Exception {
        // Initialize the database
        holdingRepository.saveAndFlush(holding);

        // Get all the holdingList where siret is not null
        defaultHoldingShouldBeFound("siret.specified=true");

        // Get all the holdingList where siret is null
        defaultHoldingShouldNotBeFound("siret.specified=false");
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
    public void getAllHoldingsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        Image image = ImageResourceIntTest.createEntity(em);
        em.persist(image);
        em.flush();
        holding.setImage(image);
        holdingRepository.saveAndFlush(holding);
        Long imageId = image.getId();

        // Get all the holdingList where image equals to imageId
        defaultHoldingShouldBeFound("imageId.equals=" + imageId);

        // Get all the holdingList where image equals to imageId + 1
        defaultHoldingShouldNotBeFound("imageId.equals=" + (imageId + 1));
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
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

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
            .siret(UPDATED_SIRET)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restHoldingMockMvc.perform(put("/api/holdings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHolding)))
            .andExpect(status().isOk());

        // Validate the Holding in the database
        List<Holding> holdingList = holdingRepository.findAll();
        assertThat(holdingList).hasSize(databaseSizeBeforeUpdate);
        Holding testHolding = holdingList.get(holdingList.size() - 1);
        assertThat(testHolding.getSiret()).isEqualTo(UPDATED_SIRET);
        assertThat(testHolding.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHolding.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

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
        when(mockHoldingSearchRepository.search(queryStringQuery("id:" + holding.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(holding), PageRequest.of(0, 1), 1));
        // Search the holding
        restHoldingMockMvc.perform(get("/api/_search/holdings?query=id:" + holding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holding.getId().intValue())))
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
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
