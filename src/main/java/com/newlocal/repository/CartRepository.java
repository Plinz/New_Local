package com.newlocal.repository;

import com.newlocal.domain.Cart;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Cart entity.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {

    @Query("select cart from Cart cart where cart.client.login = ?#{principal.username}")
    List<Cart> findByClientIsCurrentUser();
    
    @Query("select cart from Cart cart where cart.client.id = :id")
    List<Cart> getCardUser(@Param("id") Long id);

}
