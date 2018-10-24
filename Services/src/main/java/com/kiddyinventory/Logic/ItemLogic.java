package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IInventoryRepository;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.LogicInterface.IItemLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemLogic implements IItemLogic {
    private IInventoryRepository itemRepository;

    @Autowired
    public ItemLogic(IInventoryRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public Item createItem(Item item) throws IllegalArgumentException {
        //check if all item values are not null
        if(Strings.isNullOrEmpty(item.getName()) || Strings.isNullOrEmpty(item.getDescription()) || item.getCondition() == null || item.getPrice() == null || item.getPrice() == 0){
            throw new IllegalArgumentException("Values cannot be null");
        }

        Item createdItem = itemRepository.save(item);
        return createdItem;
    }

    @Override
    public void deleteItem(int itemID) {
        Optional<Item> foundItem = itemRepository.findById(itemID);

        if(!foundItem.isPresent()) {
            throw new IllegalArgumentException("Item with id : " + String.valueOf(itemID) + "not found");
        }

        itemRepository.delete(foundItem.get());
    }

    @Override
    public void updateItem(Item item) {
        Optional<Item> foundItem = itemRepository.findById(item.getItemID());

        if(!foundItem.isPresent()) {
            throw new IllegalArgumentException("Item with id : " + String.valueOf(item.getItemID()) + "not found");
        }

        itemRepository.save(item);
    }

    @Override
    public Item getItem(int itemID) {
        Optional<Item> foundItem = itemRepository.findById(itemID);

        if(!foundItem.isPresent()) {
            throw new IllegalArgumentException("Item with id : " + String.valueOf(itemID) + "not found");
        }

        return foundItem.get();
    }
}
