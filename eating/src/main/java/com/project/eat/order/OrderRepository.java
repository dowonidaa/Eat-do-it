package com.project.eat.order;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }


    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public void deleteOne(Order one) {
        em.remove(one);
    }
}
