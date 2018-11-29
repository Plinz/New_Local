package com.newlocal.web.rest;

import com.newlocal.NewLocalApp;

import com.newlocal.domain.Cart;
import com.newlocal.domain.Stock;
import com.newlocal.domain.User;
import com.newlocal.repository.CartRepository;
import com.newlocal.repository.search.CartSearchRepository;
import com.newlocal.service.CartService;
import com.newlocal.web.rest.errors.ExceptionTranslator;
import com.newlocal.service.dto.CartCriteria;
import com.newlocal.service.CartQueryService;

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
 * Test class for the CartResource REST controller.
 *
 * @see CartResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewLocalApp.class)
public class CartResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    /**
     * This repository is mocked in the com.newlocal.repository.search test package.
     *
     * @see com.newlocal.repository.search.CartSearchRepositoryMockConfiguration
     */
    @Autowired
    private CartSearchRepository mockCartSearchRepository;

    @Autowired
    private CartQueryService cartQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCartMockMvc;

    private Cart cart;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CartResource cartResource = new CartResource(cartService, cartQueryService);
        this.restCartMockMvc = MockMvcBuilders.standaloneSetup(cartResource)
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
    public static Cart createEntity(EntityManager em) {
        Cart cart = new Cart()
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        cart.setStock(stock);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        cart.setClient(user);
        return cart;
    }

    @Before
    public void initTest() {
        cart = createEntity(em);
    }

    @Test
    @Transactional
    public void createCart() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // Create the Cart
        restCartMockMvc.perform(post("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isCreated());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate + 1);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the Cart in Elasticsearch
        verify(mockCartSearchRepository, times(1)).save(testCart);
    }

    @Test
    @Transactional
    public void createCartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // Create the Cart with an existing ID
        cart.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartMockMvc.perform(post("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cart in Elasticsearch
        verify(mockCartSearchRepository, times(0)).save(cart);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setQuantity(null);

        // Create the Cart, which fails.

        restCartMockMvc.perform(post("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarts() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList
        restCartMockMvc.perform(get("/api/carts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get the cart
        restCartMockMvc.perform(get("/api/carts/{id}", cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cart.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getAllCartsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where quantity equals to DEFAULT_QUANTITY
        defaultCartShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the cartList where quantity equals to UPDATED_QUANTITY
        defaultCartShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllCartsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultCartShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the cartList where quantity equals to UPDATED_QUANTITY
        defaultCartShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllCartsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where quantity is not null
        defaultCartShouldBeFound("quantity.specified=true");

        // Get all the cartList where quantity is null
        defaultCartShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllCartsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultCartShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the cartList where quantity greater than or equals to UPDATED_QUANTITY
        defaultCartShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllCartsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where quantity less than or equals to DEFAULT_QUANTITY
        defaultCartShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the cartList where quantity less than or equals to UPDATED_QUANTITY
        defaultCartShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    // @Test
    // @Transactional
    // public void getAllCartsByStockIsEqualToSomething() throws Exception {
    //     // Initialize the database
    //     Stock stock = StockResourceIntTest.createEntity(em);
    //     em.persist(stock);
    //     em.flush();
    //     cart.setStock(stock);
    //     cartRepository.saveAndFlush(cart);
    //     Long stockId = stock.getId();
    //
    //     // Get all the cartList where stock equals to stockId
    //     defaultCartShouldBeFound("stockId.equals=" + stockId);
    //
    //     // Get all the cartList where stock equals to stockId + 1
    //     defaultCartShouldNotBeFound("stockId.equals=" + (stockId + 1));
    // }


    @Test
    @Transactional
    public void getAllCartsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        User client = UserResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        cart.setClient(client);
        cartRepository.saveAndFlush(cart);
        Long clientId = client.getId();

        // Get all the cartList where client equals to clientId
        defaultCartShouldBeFound("clientId.equals=" + clientId);

        // Get all the cartList where client equals to clientId + 1
        defaultCartShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCartShouldBeFound(String filter) throws Exception {
        restCartMockMvc.perform(get("/api/carts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restCartMockMvc.perform(get("/api/carts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCartShouldNotBeFound(String filter) throws Exception {
        restCartMockMvc.perform(get("/api/carts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartMockMvc.perform(get("/api/carts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCart() throws Exception {
        // Get the cart
        restCartMockMvc.perform(get("/api/carts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCart() throws Exception {
        // Initialize the database
        cartService.save(cart);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCartSearchRepository);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart
        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        // Disconnect from session so that the updates on updatedCart are not directly saved in db
        em.detach(updatedCart);
        updatedCart
            .quantity(UPDATED_QUANTITY);

        restCartMockMvc.perform(put("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCart)))
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the Cart in Elasticsearch
        verify(mockCartSearchRepository, times(1)).save(testCart);
    }

    @Test
    @Transactional
    public void updateNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Create the Cart

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc.perform(put("/api/carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cart in Elasticsearch
        verify(mockCartSearchRepository, times(0)).save(cart);
    }

    @Test
    @Transactional
    public void deleteCart() throws Exception {
        // Initialize the database
        cartService.save(cart);

        int databaseSizeBeforeDelete = cartRepository.findAll().size();

        // Get the cart
        restCartMockMvc.perform(delete("/api/carts/{id}", cart.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cart in Elasticsearch
        verify(mockCartSearchRepository, times(1)).deleteById(cart.getId());
    }

    @Test
    @Transactional
    public void searchCart() throws Exception {
        // Initialize the database
        cartService.save(cart);
        when(mockCartSearchRepository.search(queryStringQuery("id:" + cart.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cart), PageRequest.of(0, 1), 1));
        // Search the cart
        restCartMockMvc.perform(get("/api/_search/carts?query=id:" + cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cart.class);
        Cart cart1 = new Cart();
        cart1.setId(1L);
        Cart cart2 = new Cart();
        cart2.setId(cart1.getId());
        assertThat(cart1).isEqualTo(cart2);
        cart2.setId(2L);
        assertThat(cart1).isNotEqualTo(cart2);
        cart1.setId(null);
        assertThat(cart1).isNotEqualTo(cart2);
    }
}
