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
import com.newlocal.domain.Image;
import com.newlocal.domain.Location;
import com.newlocal.domain.Warehouse;
import com.newlocal.repository.WarehouseRepository;
import com.newlocal.repository.search.WarehouseSearchRepository;
import com.newlocal.service.WarehouseQueryService;
import com.newlocal.service.WarehouseService;
import com.newlocal.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the WarehouseResource REST controller.
 *
 * @see WarehouseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class WarehouseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private WarehouseService warehouseService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.WarehouseSearchRepositoryMockConfiguration
     */
    @Autowired
    private WarehouseSearchRepository mockWarehouseSearchRepository;

    @Autowired
    private WarehouseQueryService warehouseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWarehouseMockMvc;

    private Warehouse warehouse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WarehouseResource warehouseResource = new WarehouseResource(warehouseService, warehouseQueryService);
        this.restWarehouseMockMvc = MockMvcBuilders.standaloneSetup(warehouseResource)
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
    public static Warehouse createEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .tel(DEFAULT_TEL);
        // Add required entity
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        warehouse.setLocation(location);
        return warehouse;
    }

    @Before
    public void initTest() {
        warehouse = createEntity(em);
    }

    @Test
    @Transactional
    public void createWarehouse() throws Exception {
        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();

        // Create the Warehouse
        restWarehouseMockMvc.perform(post("/api/warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isCreated());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate + 1);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarehouse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWarehouse.getTel()).isEqualTo(DEFAULT_TEL);

        // Validate the Warehouse in Elasticsearch
        verify(mockWarehouseSearchRepository, times(1)).save(testWarehouse);
    }

    @Test
    @Transactional
    public void createWarehouseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();

        // Create the Warehouse with an existing ID
        warehouse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarehouseMockMvc.perform(post("/api/warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Warehouse in Elasticsearch
        verify(mockWarehouseSearchRepository, times(0)).save(warehouse);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setName(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc.perform(post("/api/warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setTel(null);

        // Create the Warehouse, which fails.

        restWarehouseMockMvc.perform(post("/api/warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWarehouses() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList
        restWarehouseMockMvc.perform(get("/api/warehouses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())));
    }
    
    @Test
    @Transactional
    public void getWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get the warehouse
        restWarehouseMockMvc.perform(get("/api/warehouses/{id}", warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(warehouse.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()));
    }

    @Test
    @Transactional
    public void getAllWarehousesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name equals to DEFAULT_NAME
        defaultWarehouseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the warehouseList where name equals to UPDATED_NAME
        defaultWarehouseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWarehousesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWarehouseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the warehouseList where name equals to UPDATED_NAME
        defaultWarehouseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWarehousesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name is not null
        defaultWarehouseShouldBeFound("name.specified=true");

        // Get all the warehouseList where name is null
        defaultWarehouseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllWarehousesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where description equals to DEFAULT_DESCRIPTION
        defaultWarehouseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the warehouseList where description equals to UPDATED_DESCRIPTION
        defaultWarehouseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllWarehousesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultWarehouseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the warehouseList where description equals to UPDATED_DESCRIPTION
        defaultWarehouseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllWarehousesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where description is not null
        defaultWarehouseShouldBeFound("description.specified=true");

        // Get all the warehouseList where description is null
        defaultWarehouseShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllWarehousesByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where tel equals to DEFAULT_TEL
        defaultWarehouseShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the warehouseList where tel equals to UPDATED_TEL
        defaultWarehouseShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllWarehousesByTelIsInShouldWork() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultWarehouseShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the warehouseList where tel equals to UPDATED_TEL
        defaultWarehouseShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllWarehousesByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where tel is not null
        defaultWarehouseShouldBeFound("tel.specified=true");

        // Get all the warehouseList where tel is null
        defaultWarehouseShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    public void getAllWarehousesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        Image image = ImageResourceIntTest.createEntity(em);
        em.persist(image);
        em.flush();
        warehouse.setImage(image);
        warehouseRepository.saveAndFlush(warehouse);
        Long imageId = image.getId();

        // Get all the warehouseList where image equals to imageId
        defaultWarehouseShouldBeFound("imageId.equals=" + imageId);

        // Get all the warehouseList where image equals to imageId + 1
        defaultWarehouseShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }


    @Test
    @Transactional
    public void getAllWarehousesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        warehouse.setLocation(location);
        warehouseRepository.saveAndFlush(warehouse);
        Long locationId = location.getId();

        // Get all the warehouseList where location equals to locationId
        defaultWarehouseShouldBeFound("locationId.equals=" + locationId);

        // Get all the warehouseList where location equals to locationId + 1
        defaultWarehouseShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWarehouseShouldBeFound(String filter) throws Exception {
        restWarehouseMockMvc.perform(get("/api/warehouses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())));

        // Check, that the count call also returns 1
        restWarehouseMockMvc.perform(get("/api/warehouses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWarehouseShouldNotBeFound(String filter) throws Exception {
        restWarehouseMockMvc.perform(get("/api/warehouses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWarehouseMockMvc.perform(get("/api/warehouses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingWarehouse() throws Exception {
        // Get the warehouse
        restWarehouseMockMvc.perform(get("/api/warehouses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWarehouse() throws Exception {
        // Initialize the database
        warehouseService.save(warehouse);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockWarehouseSearchRepository);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouse.getId()).get();
        // Disconnect from session so that the updates on updatedWarehouse are not directly saved in db
        em.detach(updatedWarehouse);
        updatedWarehouse
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .tel(UPDATED_TEL);

        restWarehouseMockMvc.perform(put("/api/warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWarehouse)))
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarehouse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWarehouse.getTel()).isEqualTo(UPDATED_TEL);

        // Validate the Warehouse in Elasticsearch
        verify(mockWarehouseSearchRepository, times(1)).save(testWarehouse);
    }

    @Test
    @Transactional
    public void updateNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Create the Warehouse

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc.perform(put("/api/warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Warehouse in Elasticsearch
        verify(mockWarehouseSearchRepository, times(0)).save(warehouse);
    }

    @Test
    @Transactional
    public void deleteWarehouse() throws Exception {
        // Initialize the database
        warehouseService.save(warehouse);

        int databaseSizeBeforeDelete = warehouseRepository.findAll().size();

        // Get the warehouse
        restWarehouseMockMvc.perform(delete("/api/warehouses/{id}", warehouse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Warehouse in Elasticsearch
        verify(mockWarehouseSearchRepository, times(1)).deleteById(warehouse.getId());
    }

    @Test
    @Transactional
    public void searchWarehouse() throws Exception {
        // Initialize the database
        warehouseService.save(warehouse);
        when(mockWarehouseSearchRepository.search(queryStringQuery("id:" + warehouse.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(warehouse), PageRequest.of(0, 1), 1));
        // Search the warehouse
        restWarehouseMockMvc.perform(get("/api/_search/warehouses?query=id:" + warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Warehouse.class);
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setId(1L);
        Warehouse warehouse2 = new Warehouse();
        warehouse2.setId(warehouse1.getId());
        assertThat(warehouse1).isEqualTo(warehouse2);
        warehouse2.setId(2L);
        assertThat(warehouse1).isNotEqualTo(warehouse2);
        warehouse1.setId(null);
        assertThat(warehouse1).isNotEqualTo(warehouse2);
    }
}
