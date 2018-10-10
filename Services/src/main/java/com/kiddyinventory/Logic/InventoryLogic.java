package com.kiddyinventory.Logic;

import com.kiddinventory.Entities.Account;
import com.kiddinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IInventoryRepository;
import com.kiddyinventory.LogicInterface.IInventoryLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
        Optional<Account> accountFromDb = _accountContext.findById(account.getId());
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("Account with id: " + String.valueOf(account.getId()) + "not found in the system");
        }

        account.getItems().add(item);
        _inventoryContext.save(item);
    }

    @Override
    public Item getItem(int itemId) {
        //check if item exists in the database
        Optional<Item> itemFromDb = _inventoryContext.findById(itemId);
        if(!itemFromDb.isPresent()){
            throw new IllegalArgumentException("there is no such item found in the system");
        }

        //the item has been found and returned to the user
        return itemFromDb.get();
    }

    @Override
    public Set<Item> getItemsFromAccount(int accountId) {
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

    }

    @Override
    public void deleteItems(int accountId) {

    }

    @Override
    public void moveItem(int senderId, int reveiverId, Item item) {

    }
}
