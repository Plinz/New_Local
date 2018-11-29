package com.newlocal.service;

import com.newlocal.domain.Cart;
import com.newlocal.repository.CartRepository;
import com.newlocal.repository.search.CartSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Cart.
 */
@Service
@Transactional
public class CartService {

    private final Logger log = LoggerFactory.getLogger(CartService.class);

    private CartRepository cartRepository;

    private CartSearchRepository cartSearchRepository;

    public CartService(CartRepository cartRepository, CartSearchRepository cartSearchRepository) {
        this.cartRepository = cartRepository;
        this.cartSearchRepository = cartSearchRepository;
    }

    /**
     * Save a cart.
     *
     * @param cart the entity to save
     * @return the persisted entity
     */
    public Cart save(Cart cart) {
        log.debug("Request to save Cart : {}", cart);
        Cart result = cartRepository.save(cart);
        cartSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the carts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cart> findAll(Pageable pageable) {
        log.debug("Request to get all Carts");
        return cartRepository.findAll(pageable);
    }


    /**
     * Get one cart by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Cart> findOne(Long id) {
        log.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id);
    }

    /**
     * Delete the cart by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        cartRepository.deleteById(id);
        cartSearchRepository.deleteById(id);
    }

    /**
     * Search for the cart corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cart> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Carts for query {}", query);
        return cartSearchRepository.search(queryStringQuery(query), pageable);    }
}
