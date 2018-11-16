package com.kiddyinventory.LogicInterface;

import com.kiddyinventory.Entities.Item;

import java.security.Principal;
import java.util.List;

public interface IInventoryLogic {
    /**
     * @param user the claim of the logged in user taken from JWT
     * @param accountID the id of the account to save the item to
     * @param itemID the id of the item you want to save
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if item values are empty
     * @throws IllegalArgumentException if account doesn't exists in the db
     */
    void saveItem(Principal user , int accountID , int itemID);

    /**
     * @param user the claim of the logged in user
     * @param itemId the id of the item you want to get
     * @return item the user wants to get
     * @throws IllegalArgumentException if the item doesn't exist in the database
     */
    Item getItem(Principal user, int itemId);

    /**
     * @param user the claim of the logged in user
     * @param accountId the id of the account you want to get all the items from
     * @return list of items from the given account
     * @throws IllegalArgumentException if the account doesn't exist in the database
     * @throws IllegalArgumentException if the account doesn't posses any items
     */
    List<Item> getItemsFromAccount(Principal user, int accountId);

    /**
     * @param  user the claim of the logged in user
     * @param itemId the id of the item you want to delete
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the item could not be found in de database
     */
    void deleteItem(Principal user, int itemId);

    /**
     * @param user the claim of the logged in user
     * @param accountId the id of the account you want to delete all items from
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the account doesn't exist in the database
     * @throws IllegalArgumentException if the account doesn't contain any items
     */
    void deleteItemsFromAccount(Principal user, int accountId);

    /**
     * @param user the claim of the logged in user taken from JWT token
     * @param senderId the id of the account that has the item in his inventory
     * @param receiverId the id of the account that wants to receive the item
     * @param itemID the id of the item you want to transfer
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if the item doesn't exists in the db
     * @throws IllegalArgumentException if one of the accounts doesn't exist in the database
     * @throws IllegalArgumentException if the item doesn't exist in the given sender's inventory
     */
    void moveItem(Principal user, int senderId, int receiverId, int itemID);
}
