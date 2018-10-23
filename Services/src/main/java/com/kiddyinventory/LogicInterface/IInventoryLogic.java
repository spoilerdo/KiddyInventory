package com.kiddyinventory.LogicInterface;

import com.kiddinventory.Entities.Account;
import com.kiddinventory.Entities.Item;

import java.util.Set;

public interface IInventoryLogic {
    /**
     * @param account the account you want to save the item on
     * @param item the item you want to save
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if item values are empty
     * @throws IllegalArgumentException if account doesn't exists in the db
     */
    void saveItem(Account account, Item item);

    /**
     * @param itemId the id of the item you want to get
     * @return item the user wants to get
     * @throws IllegalArgumentException if the item doesn't exist in the database
     */
    Item getItem(int itemId);

    /**
     * @param accountId the id of the account you want to get all the items from
     * @return list of items from the given account
     * @throws IllegalArgumentException if the account doesn't exist in the database
     * @throws IllegalArgumentException if the account doesn't posses any items
     */
    Set<Item> getItemsFromAccount(int accountId);

    /**
     * @param itemId the id of the item you want to delete
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the item could not be found in de database
     */
    void deleteItem(int itemId);

    /**
     * @param accountId the id of the account you want to delete all items from
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the account doesn't exist in the database
     * @throws IllegalArgumentException if the account doesn't contain any items
     */
    void deleteItemsFromAccount(int accountId);

    /**
     * @param senderId the id of the account that has the item in his inventory
     * @param receiverId the id of the account that wants to receive the item
     * @param item the item you want to transfer
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if one of the accounts doesn't exist in the database
     * @throws IllegalArgumentException if the item doesn't exist
     */
    void moveItem(int senderId, int receiverId, Item item);
}
