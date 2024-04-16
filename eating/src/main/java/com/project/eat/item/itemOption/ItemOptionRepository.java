package com.project.eat.item.itemOption;

import com.project.eat.item.ItemOption;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemOptionRepository {

    private final EntityManager em;


    public void save(ItemOption itemOption) {
        em.persist(itemOption);
    }

    public ItemOption findOne(Long itemOptionId) {
        return em.find(ItemOption.class, itemOptionId);
    }

    public void remove(ItemOption option) {
        em.remove(option);
    }
}
