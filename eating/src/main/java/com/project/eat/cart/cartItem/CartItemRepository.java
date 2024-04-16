package com.project.eat.cart.cartItem;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemRepository {

    private final EntityManager em;

    public void save(CartItem cartItem) {
        em.persist(cartItem);
    }

    public CartItem findOne(Long cartItemId) {
        return em.find(CartItem.class, cartItemId);
    }

    public void delete(CartItem cartItem) {
        em.remove(cartItem);
    }

}
