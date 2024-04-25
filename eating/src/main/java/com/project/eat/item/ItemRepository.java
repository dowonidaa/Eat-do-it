package com.project.eat.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }


    public void save(Item item) {
        em.persist(item);
    }

    public void remove(Item findItem) {
        em.remove(findItem);
    }

    public List<Item> findByShopId(Long shopId) {
      return em.createQuery("select i from Item i where i.shop.id = :shopId", Item.class)
                .setParameter("shopId", shopId)
                .getResultList();
    }


}
