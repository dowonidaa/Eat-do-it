package com.project.eat.cart.cartOption;

import com.project.eat.cart.CartItemOption;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemOptionRepository {

    private final EntityManager em;

    public void save(CartItemOption cartItemOption) {
        em.persist(cartItemOption);
    }

    public CartItemOption findOne(Long cartItemOption) {
        return em.find(CartItemOption.class, cartItemOption);
    }
}
