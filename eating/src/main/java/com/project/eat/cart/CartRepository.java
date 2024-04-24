package com.project.eat.cart;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final EntityManager em;

    public Cart findCart(Long id) {
       return em.find(Cart.class, id);
    }

    public void save(Cart cart) {
        em.persist(cart);
    }

    public void delete(Cart findCart) {
        em.remove(findCart);
    }

    public void flush() {
        em.flush();
    }
}
