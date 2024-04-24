package com.project.eat.item;

import com.project.eat.shop.ShopRepositoryEM;
import com.project.eat.shop.ShopVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final ShopRepositoryEM shopRepository;

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    @Transactional
    public void updateItem(Long itemId, ItemForm form) {
        Item findItem = itemRepository.findOne(itemId);
        ShopVO findShop = shopRepository.findShop(form.getShopId());
        findItem.setItemDescription(form.getDescription());
        findItem.setItemName(form.getName());
        findItem.setItemPrice(form.getPrice());
        findItem.setItemUrl(form.getUrl());
        findItem.setPopular(form.getPopular());
        findItem.setStatus(form.getStatus());
        findItem.setShop(findShop);

    }

    @Transactional
    public void saveItem(ItemForm form) {
        Item item = new Item();
        ShopVO shop = shopRepository.findShop(form.getShopId());
        item.setShop(shop);
        item.setItemName(form.getName());
        item.setItemPrice(form.getPrice());
        item.setItemUrl(form.getUrl());
        item.setItemDescription(form.getDescription());
        item.setStatus(form.getStatus());

        itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item findItem = itemRepository.findOne(itemId);
        if (findItem != null) {
            itemRepository.remove(findItem);
        }
    }

    public List<Item> itemList(Long shopId) {
        return itemRepository.findByShopId(shopId);
    }


}
