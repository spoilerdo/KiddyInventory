package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.LogicInterface.IItemLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@Service
public class ItemLogic implements IItemLogic {
    private IItemRepository itemRepository;

    @Autowired
    public ItemLogic(IItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public Item createItem(Item item) throws IllegalArgumentException {
        //check if all item values are not null
        CheckItemValuesNotEmpty(item);

        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(int itemID) throws IllegalArgumentException{
        //check if the item exists in the db
        Item foundItem = CheckItemExistsInDb(itemID);

        itemRepository.delete(foundItem);
    }

    @Override
    public Item updateItem(Item item) throws IllegalArgumentException {
        //check if all item values are not null
        CheckItemValuesNotEmpty(item);

        //check if the item exists in the db
        CheckItemExistsInDb(item.getItemID());

        return itemRepository.save(item);
    }

    @Override
    public Item getItem(int itemID) throws IllegalArgumentException {
        //check if the item exists in the db
        Item itemFromDb = CheckItemExistsInDb(itemID);

        return itemFromDb;
    }

    @Override
    public Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Condition> getAllConditions() {
        return Arrays.asList(Condition.values());
    }

    //region Generic exception methods
    private void CheckItemValuesNotEmpty(Item item) {
        if(Strings.isNullOrEmpty(item.getName()) || Strings.isNullOrEmpty(item.getDescription()) || item.getCondition() == null || item.getPrice() == null || item.getPrice() <= 0){
            throw new IllegalArgumentException("Values cannot be null");
        }
    }

    private Item CheckItemExistsInDb(int itemId) {
        Optional<Item> itemFromDb = itemRepository.findById(itemId);
        if(!itemFromDb.isPresent()) {
            throw new IllegalArgumentException("Item with id: " + itemId + " not found");
        }

        return itemFromDb.get();
    }
    //endregion
}
