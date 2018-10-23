package com.kiddyinventory.Logic;

import com.kiddinventory.Entities.Account;
import com.kiddinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IInventoryRepository;
import com.kiddyinventory.LogicInterface.IInventoryLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryLogic implements IInventoryLogic {
    private IInventoryRepository _inventoryContext;
    private IAccountRepository _accountContext;

    @Autowired
    public InventoryLogic(IInventoryRepository inventoryContext, IAccountRepository accountContext){
        this._inventoryContext = inventoryContext;
        this._accountContext = accountContext;
    }

    @Override
    public void saveItem(Account account, Item item) throws IllegalArgumentException {
        //check if all item values are not null
        if(Strings.isNullOrEmpty(item.getName()) || Strings.isNullOrEmpty(item.getDiscription()) || item.getCondition() == null || item.getPrice() == null || item.getPrice() == 0){
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if user is exists in de db
        CheckUserExistsInDB(account.getId());

        account.getItems().add(item);
        _inventoryContext.save(item);
    }

    @Override
    public Item getItem(int itemId) {
        //check if item exists in the database
        Optional<Item> itemFromDb = CheckItemExistsInDB(itemId);

        //the item has been found and returned to the user
        return itemFromDb.get();
    }

    @Override
    public List<Item> getItemsFromAccount(int accountId) {
        //check if the account exists in the database
        Optional<Account> accountFromDb = CheckUserExistsInDB(accountId);

        //check if the account has any items
        CheckAccountContainsItems(accountFromDb.get());

        //return all the items found from the given account
        return accountFromDb.get().getItems();
    }

    @Override
    public void deleteItem(int itemId) {
        //check if the items exists in the database
        Optional<Item> itemFormDb = CheckItemExistsInDB(itemId);

        _inventoryContext.delete(itemFormDb.get());
    }

    @Override
    public void deleteItemsFromAccount(int accountId) {
        //check if the account exists
        Optional<Account> accountFromDb = CheckUserExistsInDB(accountId);

        //check if the account contains any items
        CheckAccountContainsItems(accountFromDb.get());

        //get all items and delete them
        _inventoryContext.deleteAll(accountFromDb.get().getItems());
    }

    @Override
    public void moveItem(int senderId, int receiverId, Item item) {
        //check if the item exists in the db
        Optional<Item> itemFromDb = CheckItemExistsInDB(item.getId());

        //check if sender account exists
        Optional<Account> senderAccountFromDb = CheckUserExistsInDB(senderId);

        //check if receiver account exists
        Optional<Account> receiverAccountFromDb = CheckUserExistsInDB(receiverId);

        //check if the sender has the given item in his inventory
        if(!senderAccountFromDb.get().getItems().contains(item)){
            throw new IllegalArgumentException("sender doesn't have the given item in his inventory");
        }

        //move the item from sender to receiver
        Account sender = senderAccountFromDb.get();
        sender.getItems().remove(item);

        Account receiver = receiverAccountFromDb.get();
        receiver.getItems().add(item);

        _accountContext.save(sender);
        _accountContext.save(receiver);
    }

    private Optional<Account> CheckUserExistsInDB(int accountId){
        Optional<Account> accountFromDb = _accountContext.findById(accountId);
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("Account with id: " + String.valueOf(accountId) + " not found in the system");
        }

        return accountFromDb;
    }
    private Optional<Item> CheckItemExistsInDB(int itemId){
        Optional<Item> itemFromDb = _inventoryContext.findById(itemId);
        if(!itemFromDb.isPresent()){
            throw new IllegalArgumentException("Item with id: " + String.valueOf(itemId) + " not found in the system");
        }

        return itemFromDb;
    }
    private void CheckAccountContainsItems(Account account){
        if(account.getItems().isEmpty()){
            throw new IllegalArgumentException("Account with id: " + String.valueOf(account.getId()) + " doesn't contain any items");
        }
    }
}
