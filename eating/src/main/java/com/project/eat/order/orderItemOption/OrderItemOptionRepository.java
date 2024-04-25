package com.project.eat.order.orderItemOption;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemOptionRepository {

    private final EntityManager em;

    public void save(OrderItemOption orderItemOption) {
        em.persist(orderItemOption);
    }
}
