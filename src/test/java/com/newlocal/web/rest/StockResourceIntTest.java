package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.Stock;
import com.newlocal.domain.ProductType;
import com.newlocal.domain.Holding;
import com.newlocal.domain.User;
import com.newlocal.domain.Warehouse;
import com.newlocal.domain.Image;
import com.newlocal.repository.UserRepository;
import com.newlocal.repository.StockRepository;
import com.newlocal.repository.search.StockSearchRepository;
import com.newlocal.service.StockService;
import com.newlocal.web.rest.errors.ExceptionTranslator;
import com.newlocal.service.dto.StockCriteria;
import com.newlocal.service.StockQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class StockResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY_INIT = 0;
    private static final Integer UPDATED_QUANTITY_INIT = 1;

    private static final Integer DEFAULT_QUANTITY_REMAINING = 0;
    private static final Integer UPDATED_QUANTITY_REMAINING = 1;

    private static final Double DEFAULT_PRICE_UNIT = 0D;
    private static final Double UPDATED_PRICE_UNIT = 1D;

    private static final Instant DEFAULT_ON_SALE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ON_SALE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_BIO = false;
    private static final Boolean UPDATED_BIO = true;

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    @Autowired
    private StockRepository stockRepository;

    @Mock
    private StockRepository stockRepositoryMock;


    @Mock
    private StockService stockServiceMock;

    @Autowired
    private StockService stockService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.StockSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockSearchRepository mockStockSearchRepository;

    @Autowired
    private StockQueryService stockQueryService;

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

    private MockMvc restStockMockMvc;

    private Stock stock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockResource stockResource = new StockResource(stockService, stockQueryService, userRepository, new UserDAO(new JdbcTemplate()));
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
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
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .quantityInit(DEFAULT_QUANTITY_INIT)
            .quantityRemaining(DEFAULT_QUANTITY_REMAINING)
            .priceUnit(DEFAULT_PRICE_UNIT)
            .onSaleDate(DEFAULT_ON_SALE_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .bio(DEFAULT_BIO)
            .available(DEFAULT_AVAILABLE);
        // Add required entity
        ProductType productType = ProductTypeResourceIntTest.createEntity(em);
        em.persist(productType);
        em.flush();
        stock.setProductType(productType);
        // Add required entity
        Holding holding = HoldingResourceIntTest.createEntity(em);
        em.persist(holding);
        em.flush();
        stock.setHolding(holding);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stock.setSeller(user);
        // Add required entity
        Warehouse warehouse = WarehouseResourceIntTest.createEntity(em);
        em.persist(warehouse);
        em.flush();
        stock.setWarehouse(warehouse);
        return stock;
    }

    @Before
    public void initTest() {
        stock = createEntity(em);
    }

    // @Test
    // @Transactional
    // public void createStock() throws Exception {
    //     int databaseSizeBeforeCreate = stockRepository.findAll().size();
    //
    //     // Create the Stock
    //     restStockMockMvc.perform(post("/api/stocks")
    //         .contentType(TestUtil.APPLICATION_JSON_UTF8)
    //         .content(TestUtil.convertObjectToJsonBytes(stock)))
    //         .andExpect(status().isCreated());
    //
    //     // Validate the Stock in the database
    //     List<Stock> stockList = stockRepository.findAll();
    //     assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
    //     Stock testStock = stockList.get(stockList.size() - 1);
    //     assertThat(testStock.getName()).isEqualTo(DEFAULT_NAME);
    //     assertThat(testStock.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    //     assertThat(testStock.getQuantityInit()).isEqualTo(DEFAULT_QUANTITY_INIT);
    //     assertThat(testStock.getQuantityRemaining()).isEqualTo(DEFAULT_QUANTITY_REMAINING);
    //     assertThat(testStock.getPriceUnit()).isEqualTo(DEFAULT_PRICE_UNIT);
    //     assertThat(testStock.getOnSaleDate()).isEqualTo(DEFAULT_ON_SALE_DATE);
    //     assertThat(testStock.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
    //     assertThat(testStock.isBio()).isEqualTo(DEFAULT_BIO);
    //     assertThat(testStock.isAvailable()).isEqualTo(DEFAULT_AVAILABLE);
    //
    //     // Validate the Stock in Elasticsearch
    //     verify(mockStockSearchRepository, times(1)).save(testStock);
    // }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(0)).save(stock);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setName(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityInitIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setQuantityInit(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityRemainingIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setQuantityRemaining(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setPriceUnit(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOnSaleDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setOnSaleDate(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setExpiryDate(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBioIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setBio(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setAvailable(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantityInit").value(hasItem(DEFAULT_QUANTITY_INIT)))
            .andExpect(jsonPath("$.[*].quantityRemaining").value(hasItem(DEFAULT_QUANTITY_REMAINING)))
            .andExpect(jsonPath("$.[*].priceUnit").value(hasItem(DEFAULT_PRICE_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].onSaleDate").value(hasItem(DEFAULT_ON_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.booleanValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }

    public void getAllStocksWithEagerRelationshipsIsEnabled() throws Exception {
        StockResource stockResource = new StockResource(stockService, stockQueryService, userRepository, new UserDAO(new JdbcTemplate()));
        when(stockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStockMockMvc.perform(get("/api/stocks?eagerload=true"))
        .andExpect(status().isOk());

        verify(stockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllStocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        StockResource stockResource = new StockResource(stockService, stockQueryService, userRepository, new UserDAO(new JdbcTemplate()));
            when(stockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStockMockMvc.perform(get("/api/stocks?eagerload=true"))
        .andExpect(status().isOk());

            verify(stockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantityInit").value(DEFAULT_QUANTITY_INIT))
            .andExpect(jsonPath("$.quantityRemaining").value(DEFAULT_QUANTITY_REMAINING))
            .andExpect(jsonPath("$.priceUnit").value(DEFAULT_PRICE_UNIT.doubleValue()))
            .andExpect(jsonPath("$.onSaleDate").value(DEFAULT_ON_SALE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.booleanValue()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllStocksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where name equals to DEFAULT_NAME
        defaultStockShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stockList where name equals to UPDATED_NAME
        defaultStockShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStocksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStockShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stockList where name equals to UPDATED_NAME
        defaultStockShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStocksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where name is not null
        defaultStockShouldBeFound("name.specified=true");

        // Get all the stockList where name is null
        defaultStockShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description equals to DEFAULT_DESCRIPTION
        defaultStockShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the stockList where description equals to UPDATED_DESCRIPTION
        defaultStockShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultStockShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the stockList where description equals to UPDATED_DESCRIPTION
        defaultStockShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description is not null
        defaultStockShouldBeFound("description.specified=true");

        // Get all the stockList where description is null
        defaultStockShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityInitIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityInit equals to DEFAULT_QUANTITY_INIT
        defaultStockShouldBeFound("quantityInit.equals=" + DEFAULT_QUANTITY_INIT);

        // Get all the stockList where quantityInit equals to UPDATED_QUANTITY_INIT
        defaultStockShouldNotBeFound("quantityInit.equals=" + UPDATED_QUANTITY_INIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityInitIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityInit in DEFAULT_QUANTITY_INIT or UPDATED_QUANTITY_INIT
        defaultStockShouldBeFound("quantityInit.in=" + DEFAULT_QUANTITY_INIT + "," + UPDATED_QUANTITY_INIT);

        // Get all the stockList where quantityInit equals to UPDATED_QUANTITY_INIT
        defaultStockShouldNotBeFound("quantityInit.in=" + UPDATED_QUANTITY_INIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityInitIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityInit is not null
        defaultStockShouldBeFound("quantityInit.specified=true");

        // Get all the stockList where quantityInit is null
        defaultStockShouldNotBeFound("quantityInit.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityInitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityInit greater than or equals to DEFAULT_QUANTITY_INIT
        defaultStockShouldBeFound("quantityInit.greaterOrEqualThan=" + DEFAULT_QUANTITY_INIT);

        // Get all the stockList where quantityInit greater than or equals to UPDATED_QUANTITY_INIT
        defaultStockShouldNotBeFound("quantityInit.greaterOrEqualThan=" + UPDATED_QUANTITY_INIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityInitIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityInit less than or equals to DEFAULT_QUANTITY_INIT
        defaultStockShouldNotBeFound("quantityInit.lessThan=" + DEFAULT_QUANTITY_INIT);

        // Get all the stockList where quantityInit less than or equals to UPDATED_QUANTITY_INIT
        defaultStockShouldBeFound("quantityInit.lessThan=" + UPDATED_QUANTITY_INIT);
    }


    @Test
    @Transactional
    public void getAllStocksByQuantityRemainingIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityRemaining equals to DEFAULT_QUANTITY_REMAINING
        defaultStockShouldBeFound("quantityRemaining.equals=" + DEFAULT_QUANTITY_REMAINING);

        // Get all the stockList where quantityRemaining equals to UPDATED_QUANTITY_REMAINING
        defaultStockShouldNotBeFound("quantityRemaining.equals=" + UPDATED_QUANTITY_REMAINING);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityRemainingIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityRemaining in DEFAULT_QUANTITY_REMAINING or UPDATED_QUANTITY_REMAINING
        defaultStockShouldBeFound("quantityRemaining.in=" + DEFAULT_QUANTITY_REMAINING + "," + UPDATED_QUANTITY_REMAINING);

        // Get all the stockList where quantityRemaining equals to UPDATED_QUANTITY_REMAINING
        defaultStockShouldNotBeFound("quantityRemaining.in=" + UPDATED_QUANTITY_REMAINING);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityRemainingIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityRemaining is not null
        defaultStockShouldBeFound("quantityRemaining.specified=true");

        // Get all the stockList where quantityRemaining is null
        defaultStockShouldNotBeFound("quantityRemaining.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityRemainingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityRemaining greater than or equals to DEFAULT_QUANTITY_REMAINING
        defaultStockShouldBeFound("quantityRemaining.greaterOrEqualThan=" + DEFAULT_QUANTITY_REMAINING);

        // Get all the stockList where quantityRemaining greater than or equals to UPDATED_QUANTITY_REMAINING
        defaultStockShouldNotBeFound("quantityRemaining.greaterOrEqualThan=" + UPDATED_QUANTITY_REMAINING);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantityRemainingIsLessThanSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantityRemaining less than or equals to DEFAULT_QUANTITY_REMAINING
        defaultStockShouldNotBeFound("quantityRemaining.lessThan=" + DEFAULT_QUANTITY_REMAINING);

        // Get all the stockList where quantityRemaining less than or equals to UPDATED_QUANTITY_REMAINING
        defaultStockShouldBeFound("quantityRemaining.lessThan=" + UPDATED_QUANTITY_REMAINING);
    }


    @Test
    @Transactional
    public void getAllStocksByPriceUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where priceUnit equals to DEFAULT_PRICE_UNIT
        defaultStockShouldBeFound("priceUnit.equals=" + DEFAULT_PRICE_UNIT);

        // Get all the stockList where priceUnit equals to UPDATED_PRICE_UNIT
        defaultStockShouldNotBeFound("priceUnit.equals=" + UPDATED_PRICE_UNIT);
    }

    @Test
    @Transactional
    public void getAllStocksByPriceUnitIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where priceUnit in DEFAULT_PRICE_UNIT or UPDATED_PRICE_UNIT
        defaultStockShouldBeFound("priceUnit.in=" + DEFAULT_PRICE_UNIT + "," + UPDATED_PRICE_UNIT);

        // Get all the stockList where priceUnit equals to UPDATED_PRICE_UNIT
        defaultStockShouldNotBeFound("priceUnit.in=" + UPDATED_PRICE_UNIT);
    }

    @Test
    @Transactional
    public void getAllStocksByPriceUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where priceUnit is not null
        defaultStockShouldBeFound("priceUnit.specified=true");

        // Get all the stockList where priceUnit is null
        defaultStockShouldNotBeFound("priceUnit.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByOnSaleDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where onSaleDate equals to DEFAULT_ON_SALE_DATE
        defaultStockShouldBeFound("onSaleDate.equals=" + DEFAULT_ON_SALE_DATE);

        // Get all the stockList where onSaleDate equals to UPDATED_ON_SALE_DATE
        defaultStockShouldNotBeFound("onSaleDate.equals=" + UPDATED_ON_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllStocksByOnSaleDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where onSaleDate in DEFAULT_ON_SALE_DATE or UPDATED_ON_SALE_DATE
        defaultStockShouldBeFound("onSaleDate.in=" + DEFAULT_ON_SALE_DATE + "," + UPDATED_ON_SALE_DATE);

        // Get all the stockList where onSaleDate equals to UPDATED_ON_SALE_DATE
        defaultStockShouldNotBeFound("onSaleDate.in=" + UPDATED_ON_SALE_DATE);
    }

    @Test
    @Transactional
    public void getAllStocksByOnSaleDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where onSaleDate is not null
        defaultStockShouldBeFound("onSaleDate.specified=true");

        // Get all the stockList where onSaleDate is null
        defaultStockShouldNotBeFound("onSaleDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where expiryDate equals to DEFAULT_EXPIRY_DATE
        defaultStockShouldBeFound("expiryDate.equals=" + DEFAULT_EXPIRY_DATE);

        // Get all the stockList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultStockShouldNotBeFound("expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllStocksByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where expiryDate in DEFAULT_EXPIRY_DATE or UPDATED_EXPIRY_DATE
        defaultStockShouldBeFound("expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE);

        // Get all the stockList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultStockShouldNotBeFound("expiryDate.in=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllStocksByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where expiryDate is not null
        defaultStockShouldBeFound("expiryDate.specified=true");

        // Get all the stockList where expiryDate is null
        defaultStockShouldNotBeFound("expiryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where bio equals to DEFAULT_BIO
        defaultStockShouldBeFound("bio.equals=" + DEFAULT_BIO);

        // Get all the stockList where bio equals to UPDATED_BIO
        defaultStockShouldNotBeFound("bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllStocksByBioIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where bio in DEFAULT_BIO or UPDATED_BIO
        defaultStockShouldBeFound("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO);

        // Get all the stockList where bio equals to UPDATED_BIO
        defaultStockShouldNotBeFound("bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllStocksByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where bio is not null
        defaultStockShouldBeFound("bio.specified=true");

        // Get all the stockList where bio is null
        defaultStockShouldNotBeFound("bio.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where available equals to DEFAULT_AVAILABLE
        defaultStockShouldBeFound("available.equals=" + DEFAULT_AVAILABLE);

        // Get all the stockList where available equals to UPDATED_AVAILABLE
        defaultStockShouldNotBeFound("available.equals=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    public void getAllStocksByAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where available in DEFAULT_AVAILABLE or UPDATED_AVAILABLE
        defaultStockShouldBeFound("available.in=" + DEFAULT_AVAILABLE + "," + UPDATED_AVAILABLE);

        // Get all the stockList where available equals to UPDATED_AVAILABLE
        defaultStockShouldNotBeFound("available.in=" + UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    public void getAllStocksByAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where available is not null
        defaultStockShouldBeFound("available.specified=true");

        // Get all the stockList where available is null
        defaultStockShouldNotBeFound("available.specified=false");
    }

    @Test
    @Transactional
    public void getAllStocksByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductType productType = ProductTypeResourceIntTest.createEntity(em);
        em.persist(productType);
        em.flush();
        stock.setProductType(productType);
        stockRepository.saveAndFlush(stock);
        Long productTypeId = productType.getId();

        // Get all the stockList where productType equals to productTypeId
        defaultStockShouldBeFound("productTypeId.equals=" + productTypeId);

        // Get all the stockList where productType equals to productTypeId + 1
        defaultStockShouldNotBeFound("productTypeId.equals=" + (productTypeId + 1));
    }


    // @Test
    // @Transactional
    // public void getAllStocksByHoldingIsEqualToSomething() throws Exception {
    //     // Initialize the database
    //     Holding holding = HoldingResourceIntTest.createEntity(em);
    //     em.persist(holding);
    //     em.flush();
    //     stock.setHolding(holding);
    //     stockRepository.saveAndFlush(stock);
    //     Long holdingId = holding.getId();
    //
    //     // Get all the stockList where holding equals to holdingId
    //     defaultStockShouldBeFound("holdingId.equals=" + holdingId);
    //
    //     // Get all the stockList where holding equals to holdingId + 1
    //     defaultStockShouldNotBeFound("holdingId.equals=" + (holdingId + 1));
    // }


    @Test
    @Transactional
    public void getAllStocksBySellerIsEqualToSomething() throws Exception {
        // Initialize the database
        User seller = UserResourceIntTest.createEntity(em);
        em.persist(seller);
        em.flush();
        stock.setSeller(seller);
        stockRepository.saveAndFlush(stock);
        Long sellerId = seller.getId();

        // Get all the stockList where seller equals to sellerId
        defaultStockShouldBeFound("sellerId.equals=" + sellerId);

        // Get all the stockList where seller equals to sellerId + 1
        defaultStockShouldNotBeFound("sellerId.equals=" + (sellerId + 1));
    }


    @Test
    @Transactional
    public void getAllStocksByWarehouseIsEqualToSomething() throws Exception {
        // Initialize the database
        Warehouse warehouse = WarehouseResourceIntTest.createEntity(em);
        em.persist(warehouse);
        em.flush();
        stock.setWarehouse(warehouse);
        stockRepository.saveAndFlush(stock);
        Long warehouseId = warehouse.getId();

        // Get all the stockList where warehouse equals to warehouseId
        defaultStockShouldBeFound("warehouseId.equals=" + warehouseId);

        // Get all the stockList where warehouse equals to warehouseId + 1
        defaultStockShouldNotBeFound("warehouseId.equals=" + (warehouseId + 1));
    }


    @Test
    @Transactional
    public void getAllStocksByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        Image image = ImageResourceIntTest.createEntity(em);
        em.persist(image);
        em.flush();
        stock.addImage(image);
        stockRepository.saveAndFlush(stock);
        Long imageId = image.getId();

        // Get all the stockList where image equals to imageId
        defaultStockShouldBeFound("imageId.equals=" + imageId);

        // Get all the stockList where image equals to imageId + 1
        defaultStockShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockShouldBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantityInit").value(hasItem(DEFAULT_QUANTITY_INIT)))
            .andExpect(jsonPath("$.[*].quantityRemaining").value(hasItem(DEFAULT_QUANTITY_REMAINING)))
            .andExpect(jsonPath("$.[*].priceUnit").value(hasItem(DEFAULT_PRICE_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].onSaleDate").value(hasItem(DEFAULT_ON_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.booleanValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));

        // Check, that the count call also returns 1
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockShouldNotBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockService.save(stock);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockStockSearchRepository);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .quantityInit(UPDATED_QUANTITY_INIT)
            .quantityRemaining(UPDATED_QUANTITY_REMAINING)
            .priceUnit(UPDATED_PRICE_UNIT)
            .onSaleDate(UPDATED_ON_SALE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .bio(UPDATED_BIO)
            .available(UPDATED_AVAILABLE);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStock.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStock.getQuantityInit()).isEqualTo(UPDATED_QUANTITY_INIT);
        assertThat(testStock.getQuantityRemaining()).isEqualTo(UPDATED_QUANTITY_REMAINING);
        assertThat(testStock.getPriceUnit()).isEqualTo(UPDATED_PRICE_UNIT);
        assertThat(testStock.getOnSaleDate()).isEqualTo(UPDATED_ON_SALE_DATE);
        assertThat(testStock.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testStock.isBio()).isEqualTo(UPDATED_BIO);
        assertThat(testStock.isAvailable()).isEqualTo(UPDATED_AVAILABLE);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(1)).save(testStock);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(0)).save(stock);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Get the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(1)).deleteById(stock.getId());
    }

    @Test
    @Transactional
    public void searchStock() throws Exception {
        // Initialize the database
        stockService.save(stock);
        when(mockStockSearchRepository.search(queryStringQuery("id:" + stock.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stock), PageRequest.of(0, 1), 1));
        // Search the stock
        restStockMockMvc.perform(get("/api/_search/stocks?query=id:" + stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantityInit").value(hasItem(DEFAULT_QUANTITY_INIT)))
            .andExpect(jsonPath("$.[*].quantityRemaining").value(hasItem(DEFAULT_QUANTITY_REMAINING)))
            .andExpect(jsonPath("$.[*].priceUnit").value(hasItem(DEFAULT_PRICE_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].onSaleDate").value(hasItem(DEFAULT_ON_SALE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.booleanValue())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = new Stock();
        stock1.setId(1L);
        Stock stock2 = new Stock();
        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);
        stock2.setId(2L);
        assertThat(stock1).isNotEqualTo(stock2);
        stock1.setId(null);
        assertThat(stock1).isNotEqualTo(stock2);
    }
}
