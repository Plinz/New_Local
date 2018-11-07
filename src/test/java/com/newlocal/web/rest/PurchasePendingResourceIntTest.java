package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.PurchasePending;
import com.newlocal.repository.PurchasePendingRepository;
import com.newlocal.repository.search.PurchasePendingSearchRepository;
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
 * Test class for the PurchasePendingResource REST controller.
 *
 * @see PurchasePendingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class PurchasePendingResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private PurchasePendingRepository purchasePendingRepository;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.PurchasePendingSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchasePendingSearchRepository mockPurchasePendingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchasePendingMockMvc;

    private PurchasePending purchasePending;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchasePendingResource purchasePendingResource = new PurchasePendingResource(purchasePendingRepository, mockPurchasePendingSearchRepository);
        this.restPurchasePendingMockMvc = MockMvcBuilders.standaloneSetup(purchasePendingResource)
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
    public static PurchasePending createEntity(EntityManager em) {
        PurchasePending purchasePending = new PurchasePending()
            .quantity(DEFAULT_QUANTITY);
        return purchasePending;
    }

    @Before
    public void initTest() {
        purchasePending = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchasePending() throws Exception {
        int databaseSizeBeforeCreate = purchasePendingRepository.findAll().size();

        // Create the PurchasePending
        restPurchasePendingMockMvc.perform(post("/api/purchase-pendings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasePending)))
            .andExpect(status().isCreated());

        // Validate the PurchasePending in the database
        List<PurchasePending> purchasePendingList = purchasePendingRepository.findAll();
        assertThat(purchasePendingList).hasSize(databaseSizeBeforeCreate + 1);
        PurchasePending testPurchasePending = purchasePendingList.get(purchasePendingList.size() - 1);
        assertThat(testPurchasePending.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the PurchasePending in Elasticsearch
        verify(mockPurchasePendingSearchRepository, times(1)).save(testPurchasePending);
    }

    @Test
    @Transactional
    public void createPurchasePendingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchasePendingRepository.findAll().size();

        // Create the PurchasePending with an existing ID
        purchasePending.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchasePendingMockMvc.perform(post("/api/purchase-pendings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasePending)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasePending in the database
        List<PurchasePending> purchasePendingList = purchasePendingRepository.findAll();
        assertThat(purchasePendingList).hasSize(databaseSizeBeforeCreate);

        // Validate the PurchasePending in Elasticsearch
        verify(mockPurchasePendingSearchRepository, times(0)).save(purchasePending);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasePendingRepository.findAll().size();
        // set the field null
        purchasePending.setQuantity(null);

        // Create the PurchasePending, which fails.

        restPurchasePendingMockMvc.perform(post("/api/purchase-pendings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasePending)))
            .andExpect(status().isBadRequest());

        List<PurchasePending> purchasePendingList = purchasePendingRepository.findAll();
        assertThat(purchasePendingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchasePendings() throws Exception {
        // Initialize the database
        purchasePendingRepository.saveAndFlush(purchasePending);

        // Get all the purchasePendingList
        restPurchasePendingMockMvc.perform(get("/api/purchase-pendings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasePending.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getPurchasePending() throws Exception {
        // Initialize the database
        purchasePendingRepository.saveAndFlush(purchasePending);

        // Get the purchasePending
        restPurchasePendingMockMvc.perform(get("/api/purchase-pendings/{id}", purchasePending.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchasePending.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingPurchasePending() throws Exception {
        // Get the purchasePending
        restPurchasePendingMockMvc.perform(get("/api/purchase-pendings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchasePending() throws Exception {
        // Initialize the database
        purchasePendingRepository.saveAndFlush(purchasePending);

        int databaseSizeBeforeUpdate = purchasePendingRepository.findAll().size();

        // Update the purchasePending
        PurchasePending updatedPurchasePending = purchasePendingRepository.findById(purchasePending.getId()).get();
        // Disconnect from session so that the updates on updatedPurchasePending are not directly saved in db
        em.detach(updatedPurchasePending);
        updatedPurchasePending
            .quantity(UPDATED_QUANTITY);

        restPurchasePendingMockMvc.perform(put("/api/purchase-pendings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchasePending)))
            .andExpect(status().isOk());

        // Validate the PurchasePending in the database
        List<PurchasePending> purchasePendingList = purchasePendingRepository.findAll();
        assertThat(purchasePendingList).hasSize(databaseSizeBeforeUpdate);
        PurchasePending testPurchasePending = purchasePendingList.get(purchasePendingList.size() - 1);
        assertThat(testPurchasePending.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the PurchasePending in Elasticsearch
        verify(mockPurchasePendingSearchRepository, times(1)).save(testPurchasePending);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchasePending() throws Exception {
        int databaseSizeBeforeUpdate = purchasePendingRepository.findAll().size();

        // Create the PurchasePending

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchasePendingMockMvc.perform(put("/api/purchase-pendings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchasePending)))
            .andExpect(status().isBadRequest());

        // Validate the PurchasePending in the database
        List<PurchasePending> purchasePendingList = purchasePendingRepository.findAll();
        assertThat(purchasePendingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchasePending in Elasticsearch
        verify(mockPurchasePendingSearchRepository, times(0)).save(purchasePending);
    }

    @Test
    @Transactional
    public void deletePurchasePending() throws Exception {
        // Initialize the database
        purchasePendingRepository.saveAndFlush(purchasePending);

        int databaseSizeBeforeDelete = purchasePendingRepository.findAll().size();

        // Get the purchasePending
        restPurchasePendingMockMvc.perform(delete("/api/purchase-pendings/{id}", purchasePending.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PurchasePending> purchasePendingList = purchasePendingRepository.findAll();
        assertThat(purchasePendingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PurchasePending in Elasticsearch
        verify(mockPurchasePendingSearchRepository, times(1)).deleteById(purchasePending.getId());
    }

    @Test
    @Transactional
    public void searchPurchasePending() throws Exception {
        // Initialize the database
        purchasePendingRepository.saveAndFlush(purchasePending);
        when(mockPurchasePendingSearchRepository.search(queryStringQuery("id:" + purchasePending.getId())))
            .thenReturn(Collections.singletonList(purchasePending));
        // Search the purchasePending
        restPurchasePendingMockMvc.perform(get("/api/_search/purchase-pendings?query=id:" + purchasePending.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchasePending.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchasePending.class);
        PurchasePending purchasePending1 = new PurchasePending();
        purchasePending1.setId(1L);
        PurchasePending purchasePending2 = new PurchasePending();
        purchasePending2.setId(purchasePending1.getId());
        assertThat(purchasePending1).isEqualTo(purchasePending2);
        purchasePending2.setId(2L);
        assertThat(purchasePending1).isNotEqualTo(purchasePending2);
        purchasePending1.setId(null);
        assertThat(purchasePending1).isNotEqualTo(purchasePending2);
    }
}
