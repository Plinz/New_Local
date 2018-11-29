package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.Image;
import com.newlocal.domain.Stock;
import com.newlocal.domain.Category;
import com.newlocal.domain.ProductType;
import com.newlocal.domain.Holding;
import com.newlocal.domain.Warehouse;
import com.newlocal.repository.ImageRepository;
import com.newlocal.repository.search.ImageSearchRepository;
import com.newlocal.service.ImageService;
import com.newlocal.web.rest.errors.ExceptionTranslator;
import com.newlocal.service.dto.ImageCriteria;
import com.newlocal.service.ImageQueryService;

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
 * Test class for the ImageResource REST controller.
 *
 * @see ImageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class ImageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private ImageService imageService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.ImageSearchRepositoryMockConfiguration
     */
    @Autowired
    private ImageSearchRepository mockImageSearchRepository;

    @Autowired
    private ImageQueryService imageQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImageMockMvc;

    private Image image;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ImageResource imageResource = new ImageResource(imageService, imageQueryService);
        this.restImageMockMvc = MockMvcBuilders.standaloneSetup(imageResource)
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
    public static Image createEntity(EntityManager em) {
        Image image = new Image()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return image;
    }

    @Before
    public void initTest() {
        image = createEntity(em);
    }

    @Test
    @Transactional
    public void createImage() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image
        restImageMockMvc.perform(post("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isCreated());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate + 1);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Image in Elasticsearch
        verify(mockImageSearchRepository, times(1)).save(testImage);
    }

    @Test
    @Transactional
    public void createImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = imageRepository.findAll().size();

        // Create the Image with an existing ID
        image.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageMockMvc.perform(post("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeCreate);

        // Validate the Image in Elasticsearch
        verify(mockImageSearchRepository, times(0)).save(image);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageRepository.findAll().size();
        // set the field null
        image.setName(null);

        // Create the Image, which fails.

        restImageMockMvc.perform(post("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isBadRequest());

        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImages() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList
        restImageMockMvc.perform(get("/api/images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getImage() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(image.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getAllImagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name equals to DEFAULT_NAME
        defaultImageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the imageList where name equals to UPDATED_NAME
        defaultImageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllImagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultImageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the imageList where name equals to UPDATED_NAME
        defaultImageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllImagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where name is not null
        defaultImageShouldBeFound("name.specified=true");

        // Get all the imageList where name is null
        defaultImageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllImagesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description equals to DEFAULT_DESCRIPTION
        defaultImageShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the imageList where description equals to UPDATED_DESCRIPTION
        defaultImageShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllImagesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultImageShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the imageList where description equals to UPDATED_DESCRIPTION
        defaultImageShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllImagesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        imageRepository.saveAndFlush(image);

        // Get all the imageList where description is not null
        defaultImageShouldBeFound("description.specified=true");

        // Get all the imageList where description is null
        defaultImageShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllImagesByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        image.addStock(stock);
        imageRepository.saveAndFlush(image);
        Long stockId = stock.getId();

        // Get all the imageList where stock equals to stockId
        defaultImageShouldBeFound("stockId.equals=" + stockId);

        // Get all the imageList where stock equals to stockId + 1
        defaultImageShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }


    @Test
    @Transactional
    public void getAllImagesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        image.addCategory(category);
        imageRepository.saveAndFlush(image);
        Long categoryId = category.getId();

        // Get all the imageList where category equals to categoryId
        defaultImageShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the imageList where category equals to categoryId + 1
        defaultImageShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllImagesByProductTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ProductType productType = ProductTypeResourceIntTest.createEntity(em);
        em.persist(productType);
        em.flush();
        image.addProductType(productType);
        imageRepository.saveAndFlush(image);
        Long productTypeId = productType.getId();

        // Get all the imageList where productType equals to productTypeId
        defaultImageShouldBeFound("productTypeId.equals=" + productTypeId);

        // Get all the imageList where productType equals to productTypeId + 1
        defaultImageShouldNotBeFound("productTypeId.equals=" + (productTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllImagesByHoldingIsEqualToSomething() throws Exception {
        // Initialize the database
        Holding holding = HoldingResourceIntTest.createEntity(em);
        em.persist(holding);
        em.flush();
        image.addHolding(holding);
        imageRepository.saveAndFlush(image);
        Long holdingId = holding.getId();

        // Get all the imageList where holding equals to holdingId
        defaultImageShouldBeFound("holdingId.equals=" + holdingId);

        // Get all the imageList where holding equals to holdingId + 1
        defaultImageShouldNotBeFound("holdingId.equals=" + (holdingId + 1));
    }


    @Test
    @Transactional
    public void getAllImagesByWarehouseIsEqualToSomething() throws Exception {
        // Initialize the database
        Warehouse warehouse = WarehouseResourceIntTest.createEntity(em);
        em.persist(warehouse);
        em.flush();
        image.addWarehouse(warehouse);
        imageRepository.saveAndFlush(image);
        Long warehouseId = warehouse.getId();

        // Get all the imageList where warehouse equals to warehouseId
        defaultImageShouldBeFound("warehouseId.equals=" + warehouseId);

        // Get all the imageList where warehouse equals to warehouseId + 1
        defaultImageShouldNotBeFound("warehouseId.equals=" + (warehouseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultImageShouldBeFound(String filter) throws Exception {
        restImageMockMvc.perform(get("/api/images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restImageMockMvc.perform(get("/api/images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultImageShouldNotBeFound(String filter) throws Exception {
        restImageMockMvc.perform(get("/api/images?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageMockMvc.perform(get("/api/images/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingImage() throws Exception {
        // Get the image
        restImageMockMvc.perform(get("/api/images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImage() throws Exception {
        // Initialize the database
        imageService.save(image);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockImageSearchRepository);

        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Update the image
        Image updatedImage = imageRepository.findById(image.getId()).get();
        // Disconnect from session so that the updates on updatedImage are not directly saved in db
        em.detach(updatedImage);
        updatedImage
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restImageMockMvc.perform(put("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedImage)))
            .andExpect(status().isOk());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);
        Image testImage = imageList.get(imageList.size() - 1);
        assertThat(testImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Image in Elasticsearch
        verify(mockImageSearchRepository, times(1)).save(testImage);
    }

    @Test
    @Transactional
    public void updateNonExistingImage() throws Exception {
        int databaseSizeBeforeUpdate = imageRepository.findAll().size();

        // Create the Image

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageMockMvc.perform(put("/api/images")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(image)))
            .andExpect(status().isBadRequest());

        // Validate the Image in the database
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Image in Elasticsearch
        verify(mockImageSearchRepository, times(0)).save(image);
    }

    @Test
    @Transactional
    public void deleteImage() throws Exception {
        // Initialize the database
        imageService.save(image);

        int databaseSizeBeforeDelete = imageRepository.findAll().size();

        // Get the image
        restImageMockMvc.perform(delete("/api/images/{id}", image.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Image> imageList = imageRepository.findAll();
        assertThat(imageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Image in Elasticsearch
        verify(mockImageSearchRepository, times(1)).deleteById(image.getId());
    }

    @Test
    @Transactional
    public void searchImage() throws Exception {
        // Initialize the database
        imageService.save(image);
        when(mockImageSearchRepository.search(queryStringQuery("id:" + image.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(image), PageRequest.of(0, 1), 1));
        // Search the image
        restImageMockMvc.perform(get("/api/_search/images?query=id:" + image.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(image.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Image.class);
        Image image1 = new Image();
        image1.setId(1L);
        Image image2 = new Image();
        image2.setId(image1.getId());
        assertThat(image1).isEqualTo(image2);
        image2.setId(2L);
        assertThat(image1).isNotEqualTo(image2);
        image1.setId(null);
        assertThat(image1).isNotEqualTo(image2);
    }
}
