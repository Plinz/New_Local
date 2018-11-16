package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.Purchase;
import com.newlocal.domain.Stock;
import com.newlocal.domain.User;
import com.newlocal.repository.PurchaseRepository;
import com.newlocal.repository.search.PurchaseSearchRepository;
import com.newlocal.service.PurchaseService;
import com.newlocal.web.rest.errors.ExceptionTranslator;
import com.newlocal.service.dto.PurchaseCriteria;
import com.newlocal.service.PurchaseQueryService;

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
 * Test class for the PurchaseResource REST controller.
 *
 * @see PurchaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class PurchaseResourceIntTest {

    private static final Instant DEFAULT_SALE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SALE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private PurchaseRepository purchaseRepository;
    
    @Autowired
    private PurchaseService purchaseService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.PurchaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchaseSearchRepository mockPurchaseSearchRepository;

    @Autowired
    private PurchaseQueryService purchaseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseResource purchaseResource = new PurchaseResource(purchaseService, purchaseQueryService);
        this.restPurchaseMockMvc = MockMvcBuilders.standaloneSetup(purchaseResource)
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
    public static Purchase createEntity(EntityManager em) {
        Purchase purchase = new Purchase()
            .saleDate(DEFAULT_SALE_DATE)
            .quantity(DEFAULT_QUANTITY);
        return purchase;
    }

    @Before
    public void initTest() {
        purchase = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchase() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase
        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchase)))
            .andExpect(status().isCreated());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeCreate + 1);
        Purchase testPurchase = purchaseList.get(purchaseList.size() - 1);
        assertThat(testPurchase.getSaleDate()).isEqualTo(DEFAULT_SALE_DATE);
        assertThat(testPurchase.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(1)).save(testPurchase);
    }

    @Test
    @Transactional
    public void createPurchaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase with an existing ID
        purchase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchase)))
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(0)).save(purchase);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setQuantity(null);

        // Create the Purchase, which fails.

        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchase)))
            .andExpect(status().isBadRequest());

        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllpurchases() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getpurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchase.getId().intValue()))
            .andExpect(jsonPath("$.saleDate").value(DEFAULT_SALE_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getAllpurchasesBySaleDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where saleDate equals to DEFAULT_SALE_DATE
        defaultpurchaseShouldBeFound("saleDate.equals=" + DEFAULT_SALE_DATE);

        // Get all the purchaseList where saleDate equals to UPDATED_SALE_DATE
        defaultpurchaseShouldNotBeFound("saleDate.equals=" + UPDATED_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllpurchasesBySaleDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where saleDate in DEFAULT_SALE_DATE or UPDATED_SALE_DATE
        defaultpurchaseShouldBeFound("saleDate.in=" + DEFAULT_SALE_DATE + "," + UPDATED_SALE_DATE);

        // Get all the purchaseList where saleDate equals to UPDATED_SALE_DATE
        defaultpurchaseShouldNotBeFound("saleDate.in=" + UPDATED_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllpurchasesBySaleDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where saleDate is not null
        defaultpurchaseShouldBeFound("saleDate.specified=true");

        // Get all the purchaseList where saleDate is null
        defaultpurchaseShouldNotBeFound("saleDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllpurchasesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where quantity equals to DEFAULT_QUANTITY
        defaultpurchaseShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the purchaseList where quantity equals to UPDATED_QUANTITY
        defaultpurchaseShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllpurchasesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultpurchaseShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the purchaseList where quantity equals to UPDATED_QUANTITY
        defaultpurchaseShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllpurchasesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where quantity is not null
        defaultpurchaseShouldBeFound("quantity.specified=true");

        // Get all the purchaseList where quantity is null
        defaultpurchaseShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllpurchasesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultpurchaseShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the purchaseList where quantity greater than or equals to UPDATED_QUANTITY
        defaultpurchaseShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllpurchasesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList where quantity less than or equals to DEFAULT_QUANTITY
        defaultpurchaseShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the purchaseList where quantity less than or equals to UPDATED_QUANTITY
        defaultpurchaseShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllpurchasesByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        purchase.setStock(stock);
        purchaseRepository.saveAndFlush(purchase);
        Long stockId = stock.getId();

        // Get all the purchaseList where stock equals to stockId
        defaultpurchaseShouldBeFound("stockId.equals=" + stockId);

        // Get all the purchaseList where stock equals to stockId + 1
        defaultpurchaseShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }


    @Test
    @Transactional
    public void getAllpurchasesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        User client = UserResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        purchase.setClient(client);
        purchaseRepository.saveAndFlush(purchase);
        Long clientId = client.getId();

        // Get all the purchaseList where client equals to clientId
        defaultpurchaseShouldBeFound("clientId.equals=" + clientId);

        // Get all the purchaseList where client equals to clientId + 1
        defaultpurchaseShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultpurchaseShouldBeFound(String filter) throws Exception {
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restPurchaseMockMvc.perform(get("/api/purchases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultpurchaseShouldNotBeFound(String filter) throws Exception {
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseMockMvc.perform(get("/api/purchases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingpurchase() throws Exception {
        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatepurchase() throws Exception {
        // Initialize the database
        purchaseService.save(purchase);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPurchaseSearchRepository);

        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Update the purchase
        Purchase updatedpurchase = purchaseRepository.findById(purchase.getId()).get();
        // Disconnect from session so that the updates on updatedpurchase are not directly saved in db
        em.detach(updatedpurchase);
        updatedpurchase
            .saleDate(UPDATED_SALE_DATE)
            .quantity(UPDATED_QUANTITY);

        restPurchaseMockMvc.perform(put("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedpurchase)))
            .andExpect(status().isOk());

        // Validate the purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeUpdate);
        Purchase testpurchase = purchaseList.get(purchaseList.size() - 1);
        assertThat(testpurchase.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);
        assertThat(testpurchase.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(1)).save(testpurchase);
    }

    @Test
    @Transactional
    public void updateNonExistingpurchase() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Create the purchase

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseMockMvc.perform(put("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchase)))
            .andExpect(status().isBadRequest());

        // Validate the purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(0)).save(purchase);
    }

    @Test
    @Transactional
    public void deletepurchase() throws Exception {
        // Initialize the database
        purchaseService.save(purchase);

        int databaseSizeBeforeDelete = purchaseRepository.findAll().size();

        // Get the purchase
        restPurchaseMockMvc.perform(delete("/api/purchases/{id}", purchase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the purchase in Elasticsearch
        verify(mockPurchaseSearchRepository, times(1)).deleteById(purchase.getId());
    }

    @Test
    @Transactional
    public void searchpurchase() throws Exception {
        // Initialize the database
        purchaseService.save(purchase);
        when(mockPurchaseSearchRepository.search(queryStringQuery("id:" + purchase.getId())))
            .thenReturn(Collections.singletonList(purchase));
        // Search the purchase
        restPurchaseMockMvc.perform(get("/api/_search/purchases?query=id:" + purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].saleDate").value(hasItem(DEFAULT_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Purchase.class);
        Purchase purchase1 = new Purchase();
        purchase1.setId(1L);
        Purchase purchase2 = new Purchase();
        purchase2.setId(purchase1.getId());
        assertThat(purchase1).isEqualTo(purchase2);
        purchase2.setId(2L);
        assertThat(purchase1).isNotEqualTo(purchase2);
        purchase1.setId(null);
        assertThat(purchase1).isNotEqualTo(purchase2);
    }
}
