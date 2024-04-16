package com.project.eat.item.itemOption;

import com.project.eat.item.Item;
import com.project.eat.item.ItemOption;
import com.project.eat.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemOptionService {

    private final ItemOptionRepository itemOptionRepository;
    private final ItemRepository itemRepository;


    @Transactional
    public void updateItemOption(Long itemOptionId, ItemOptionForm form) {
        ItemOption findOption = itemOptionRepository.findOne(itemOptionId);
        findOption.setContent(form.getContent());
        findOption.setPrice(form.getPrice());
    }

    @Transactional
    public void saveItemOption(ItemOptionForm form) {
        ItemOption saveItemOption = new ItemOption();
        Item findItem = itemRepository.findOne(form.getItemId());
        saveItemOption.setItem(findItem);
        saveItemOption.setContent(form.getContent());
        saveItemOption.setPrice(form.getPrice());
        itemOptionRepository.save(saveItemOption);
    }

    @Transactional
    public void deleteItemOption(Long itemOptionId) {
        ItemOption option = itemOptionRepository.findOne(itemOptionId);
        if (option != null) {
            itemOptionRepository.remove(option);
        }
    }

    public ItemOption findOne(Long itemOptionId) {
        return itemOptionRepository.findOne(itemOptionId);
    }
}
