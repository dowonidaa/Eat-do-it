package com.project.eat.shop;

import com.project.eat.shop.ShopVO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryEM {

    private final EntityManager em;

    public ShopVO findShop(Long shopId) {
        return em.find(ShopVO.class, shopId);
    }

    public List<ShopVO> findAll() {
        return em.createQuery("select s from ShopVO s", ShopVO.class).getResultList();
    }
}
