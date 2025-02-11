package com.kiddyinventory.LogicInterface;

import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.Enums.Condition;

import java.util.List;

public interface IItemLogic {
    /**
     * create item with given parameter
     * @param item the item you want to create
     * @return the created item.
     * @throws IllegalArgumentException if item values are empty
     */
    Item createItem(Item item);
    /**
     * delete item with given parameter
     * @param itemID the id of the item you want to delete
     * @return nothing
     * @throws IllegalArgumentException if item is not found in the system
     */
    void deleteItem(int itemID);
    /**
     * update item with given parameter
     * @param item the item you want to update
     * @return nothing
     * @throws IllegalArgumentException if item values are empty
     * @throws IllegalArgumentException if item is not found in the system
     */
    Item updateItem(Item item);
    /**
     * get item with given parameter
     * @param itemID the id of the item you want to find
     * @return the found item
     * @throws IllegalArgumentException if item is not found in the system
     */
    Item getItem(int itemID);

    /**
     * get all items
     * @return a list of items
     */
    Iterable<Item> getAllItems();

    /**
     * get all conditions
     * @return a list of the conditions
     */
    List<Condition> getAllConditions();
}
