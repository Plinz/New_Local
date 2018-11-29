package com.newlocal.repository;

import com.newlocal.domain.Cart;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {

    @Query("select cart from Cart cart where cart.client.login = ?#{principal.username}")
    List<Cart> findByClientIsCurrentUser();

}
