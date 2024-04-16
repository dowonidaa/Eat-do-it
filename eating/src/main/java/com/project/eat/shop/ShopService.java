package com.project.eat.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {



    private final ShopRepositoryEM shopRepositoryEM;


    public ShopVO findShop(Long shopId) {
        return shopRepositoryEM.findShop(shopId);
    }

    public List<ShopVO> findAll() {
        return shopRepositoryEM.findAll();
    }

}
