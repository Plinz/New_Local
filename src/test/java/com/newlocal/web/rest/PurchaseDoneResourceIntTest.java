package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.PurchaseDone;
import com.newlocal.repository.PurchaseDoneRepository;
import com.newlocal.repository.search.PurchaseDoneSearchRepository;
import com.newlocal.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the PurchaseDoneResource REST controller.
 *
 * @see PurchaseDoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class PurchaseDoneResourceIntTest {

    private static final Instant DEFAULT_SALE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SALE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private PurchaseDoneRepository purchaseDoneRepository;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.PurchaseDoneSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchaseDoneSearchRepository mockPurchaseDoneSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseDoneMockMvc;

    private PurchaseDone purchaseDone;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseDoneResource purchaseDoneResource = new PurchaseDoneResource(purchaseDoneRepository, mockPurchaseDoneSearchRepository);
        this.restPurchaseDoneMockMvc = MockMvcBuilders.standaloneSetup(purchaseDoneResource)
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
    public static PurchaseDone createEntity(EntityManager em) {
        PurchaseDone purchaseDone = new PurchaseDone()
            .saleDate(DEFAULT_SALE_DATE)
            .quantity(DEFAULT_QUANTITY);
        return purchaseDone;
    }

    @Before
    public void initTest() {
        purchaseDone = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseDone() throws Exception {
        int databaseSizeBeforeCreate = purchaseDoneRepository.findAll().size();

        // Create the PurchaseDone
        restPurchaseDoneMockMvc.perform(post("/api/purchase-dones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDone)))
            .andExpect(status().isCreated());

        // Validate the PurchaseDone in the database
        List<PurchaseDone> purchaseDoneList = purchaseDoneRepository.findAll();
        assertThat(purchaseDoneList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseDone testPurchaseDone = purchaseDoneList.get(purchaseDoneList.size() - 1);
        assertThat(testPurchaseDone.getSaleDate()).isEqualTo(DEFAULT_SALE_DATE);
        assertThat(testPurchaseDone.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the PurchaseDone in Elasticsearch
        verify(mockPurchaseDoneSearchRepository, times(1)).save(testPurchaseDone);
    }

    @Test
    @Transactional
    public void createPurchaseDoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseDoneRepository.findAll().size();

        // Create the PurchaseDone with an existing ID
        purchaseDone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseDoneMockMvc.perform(post("/api/purchase-dones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDone)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseDone in the database
        List<PurchaseDone> purchaseDoneList = purchaseDoneRepository.findAll();
        assertThat(purchaseDoneList).hasSize(databaseSizeBeforeCreate);

        // Validate the PurchaseDone in Elasticsearch
        verify(mockPurchaseDoneSearchRepository, times(0)).save(purchaseDone);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseDoneRepository.findAll().size();
        // set the field null
        purchaseDone.setQuantity(null);

        // Create the PurchaseDone, which fails.

        restPurchaseDoneMockMvc.perform(post("/api/purchase-dones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDone)))
            .andExpect(status().isBadRequest());

        List<PurchaseDone> purchaseDoneList = purchaseDoneRepository.findAll();
        assertThat(purchaseDoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseDones() throws Exception {
        // Initialize the database
        purchaseDoneRepository.saveAndFlush(purchaseDone);

        // Get all the purchaseDoneList
        restPurchaseDoneMockMvc.perform(get("/api/purchase-dones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseDone.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getPurchaseDone() throws Exception {
        // Initialize the database
        purchaseDoneRepository.saveAndFlush(purchaseDone);

        // Get the purchaseDone
        restPurchaseDoneMockMvc.perform(get("/api/purchase-dones/{id}", purchaseDone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseDone.getId().intValue()))
            .andExpect(jsonPath("$.saleDate").value(DEFAULT_SALE_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseDone() throws Exception {
        // Get the purchaseDone
        restPurchaseDoneMockMvc.perform(get("/api/purchase-dones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseDone() throws Exception {
        // Initialize the database
        purchaseDoneRepository.saveAndFlush(purchaseDone);

        int databaseSizeBeforeUpdate = purchaseDoneRepository.findAll().size();

        // Update the purchaseDone
        PurchaseDone updatedPurchaseDone = purchaseDoneRepository.findById(purchaseDone.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseDone are not directly saved in db
        em.detach(updatedPurchaseDone);
        updatedPurchaseDone
            .saleDate(UPDATED_SALE_DATE)
            .quantity(UPDATED_QUANTITY);

        restPurchaseDoneMockMvc.perform(put("/api/purchase-dones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseDone)))
            .andExpect(status().isOk());

        // Validate the PurchaseDone in the database
        List<PurchaseDone> purchaseDoneList = purchaseDoneRepository.findAll();
        assertThat(purchaseDoneList).hasSize(databaseSizeBeforeUpdate);
        PurchaseDone testPurchaseDone = purchaseDoneList.get(purchaseDoneList.size() - 1);
        assertThat(testPurchaseDone.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);
        assertThat(testPurchaseDone.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the PurchaseDone in Elasticsearch
        verify(mockPurchaseDoneSearchRepository, times(1)).save(testPurchaseDone);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseDone() throws Exception {
        int databaseSizeBeforeUpdate = purchaseDoneRepository.findAll().size();

        // Create the PurchaseDone

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseDoneMockMvc.perform(put("/api/purchase-dones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDone)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseDone in the database
        List<PurchaseDone> purchaseDoneList = purchaseDoneRepository.findAll();
        assertThat(purchaseDoneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseDone in Elasticsearch
        verify(mockPurchaseDoneSearchRepository, times(0)).save(purchaseDone);
    }

    @Test
    @Transactional
    public void deletePurchaseDone() throws Exception {
        // Initialize the database
        purchaseDoneRepository.saveAndFlush(purchaseDone);

        int databaseSizeBeforeDelete = purchaseDoneRepository.findAll().size();

        // Get the purchaseDone
        restPurchaseDoneMockMvc.perform(delete("/api/purchase-dones/{id}", purchaseDone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchaseDone> purchaseDoneList = purchaseDoneRepository.findAll();
        assertThat(purchaseDoneList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PurchaseDone in Elasticsearch
        verify(mockPurchaseDoneSearchRepository, times(1)).deleteById(purchaseDone.getId());
    }

    @Test
    @Transactional
    public void searchPurchaseDone() throws Exception {
        // Initialize the database
        purchaseDoneRepository.saveAndFlush(purchaseDone);
        when(mockPurchaseDoneSearchRepository.search(queryStringQuery("id:" + purchaseDone.getId())))
            .thenReturn(Collections.singletonList(purchaseDone));
        // Search the purchaseDone
        restPurchaseDoneMockMvc.perform(get("/api/_search/purchase-dones?query=id:" + purchaseDone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseDone.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseDone.class);
        PurchaseDone purchaseDone1 = new PurchaseDone();
        purchaseDone1.setId(1L);
        PurchaseDone purchaseDone2 = new PurchaseDone();
        purchaseDone2.setId(purchaseDone1.getId());
        assertThat(purchaseDone1).isEqualTo(purchaseDone2);
        purchaseDone2.setId(2L);
        assertThat(purchaseDone1).isNotEqualTo(purchaseDone2);
        purchaseDone1.setId(null);
        assertThat(purchaseDone1).isNotEqualTo(purchaseDone2);
    }
}
