package com.kiddyinventory.Logic;

import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.LogicInterface.IInventoryLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryLogic implements IInventoryLogic {
    private IItemRepository _itemContext;
    private IAccountRepository _accountContext;

    @Autowired
    public InventoryLogic(IItemRepository inventoryContext, IAccountRepository accountContext){
        this._itemContext = inventoryContext;
        this._accountContext = accountContext;
    }

    @Override
    public void saveItem(Account account, Item item) throws IllegalArgumentException {
        //check if all item values are not null
        if(Strings.isNullOrEmpty(item.getName()) || Strings.isNullOrEmpty(item.getDescription()) || item.getCondition() == null || item.getPrice() == null || item.getPrice() == 0){
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if user is exists in de db
        Optional<Account> accountFromDb = _accountContext.findById(account.getAccountID());
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("Account with id: " + String.valueOf(account.getAccountID()) + "not found in the system");
        }

        account.getItems().add(item);
        _itemContext.save(item);
    }

    @Override
    public Item getItem(int itemId) {
        //check if item exists in the database
        Optional<Item> itemFromDb = _itemContext.findById(itemId);
        if(!itemFromDb.isPresent()){
            throw new IllegalArgumentException("there is no such item found in the system");
        }

        //the item has been found and returned to the user
        return itemFromDb.get();
    }

    @Override
    public List<Item> getItemsFromAccount(int accountId) {
        //check if the account exists in the database
        Optional<Account> accountFromDb = _accountContext.findById(accountId);
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("can't find account in the system");
        }

        //check if the account posses any items
        if(accountFromDb.get().getItems().isEmpty()){
            throw new IllegalArgumentException("the given account doesn't contain any items");
        }

        //return all the items found from the given account
        return accountFromDb.get().getItems();
    }

    @Override
    public void deleteItem(int itemId) {
        //check if the items exists in the database
        Optional<Item> itemFormDb = _itemContext.findById(itemId);
        if(!itemFormDb.isPresent()){
            throw new IllegalArgumentException("item could no be found and therefor not be deleted");
        }

        _itemContext.delete(itemFormDb.get());
    }

    @Override
    public void deleteItemsFromAccount(int accountId) {
        //TODO: sommige regels zijn niet SOLID omdat ze steeds herhaald worden. Dit is miss ook het geval in de BankAPI
        //check if the account exists
        Optional<Account> accountFromDb = _accountContext.findById(accountId);
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("can't find account in the system");
        }

        //check if the account contains any items
        if(accountFromDb.get().getItems().isEmpty()){
            throw new IllegalArgumentException("the given account doesn't contain any items");
        }

        //get all items and delete them
        _itemContext.deleteAll(accountFromDb.get().getItems());
    }

    @Override
    public void moveItem(int senderId, int receiverId, Item item) {
        //check if sender account exists
        Optional<Account> senderAccountFromDb = _accountContext.findById(senderId);
        if(!senderAccountFromDb.isPresent()){
            throw new IllegalArgumentException("sender account doesn't exist");
        }

        //check if receiver account exists
        Optional<Account> receiverAccountFromDb = _accountContext.findById(receiverId);
        if(!receiverAccountFromDb.isPresent()){
            throw new IllegalArgumentException("receiver account doesn't exist");
        }

        //check if the item exists //TODO: volgens mij kijkt hij nu naar de items in de item database en niet in de lijst van item in de sender account
        Optional<Item> itemFromDb = _itemContext.findById(item.getItemID());
        if(!itemFromDb.isPresent()){
            throw new IllegalArgumentException("there is no such item found in the system");
        }

        //TODO: AFMAKEN!!
    }
}
