package com.kiddyinventory.Logic;

import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.DataInterfaces.IItemRepository;
import com.kiddyinventory.Helper.RestCallHelper;
import com.kiddyinventory.LogicInterface.IInventoryLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.kiddyinventory.Constants.AuthConstants.*;

@Service
public class InventoryLogic implements IInventoryLogic {
    private IItemRepository _itemContext;
    private RestCallHelper restCallHelper;
    private IAccountRepository _accountContext;

    @Autowired
    public InventoryLogic(IItemRepository itemContext, IAccountRepository accountContext, RestCallHelper restCallHelper){
        this._itemContext = itemContext;
        this._accountContext = accountContext;
        this.restCallHelper = restCallHelper;
    }

    @Override
    public void saveItem(Principal user , int accountID, int itemID) throws IllegalArgumentException {
        Item foundItem = checkItemExistsInDb(itemID);

        checkUserMatches(user.getName(), accountID);

        //check if user exists in de db
        Account foundAccount = checkUserExistsInDb(accountID);

        foundAccount.getItems().add(foundItem);
        foundItem.getAccounts().add(foundAccount);

        _accountContext.save(foundAccount);
    }

    @Override
    public Item getItem(Principal user, int itemId) {
        //check if item exists in the database
        return checkAccountHasItem(user.getName(), itemId);
    }

    @Override
    public List<Item> getItemsFromAccount(Principal user, int accountId) {
        checkUserMatches(user.getName(), accountId);

        //check if the account exists in the database
        Account accountFromDb = checkUserExistsInDb(accountId);

        //check if the account has any items
        checkAccountContainsItems(accountFromDb);

        //return all the items found from the given account
        return accountFromDb.getItems();
    }

    @Override
    public void deleteItem(Principal user, int itemId) {
        //check if the items exists in the database
        Item itemFormDb = checkAccountHasItem(user.getName(), itemId);

        _itemContext.delete(itemFormDb);
    }

    @Override
    public void deleteItemsFromAccount(Principal user, int accountId) {
        //check if he has access to make this call
        checkUserMatches(user.getName(), accountId);

        //check if the account exists
        Account accountFromDb = checkUserExistsInDb(accountId);

        //check if the account contains any items
        checkAccountContainsItems(accountFromDb);

        //get all items and delete them
        _itemContext.deleteAll(accountFromDb.getItems());
    }

    @Override
    public void moveItem(Principal user, int senderId, int receiverId, int itemID) {
        //check if the item exists in the db
        Item itemFromDb = checkAccountHasItem(user.getName() ,itemID);

        checkUserMatches(user.getName(), senderId);

        //check if sender account exists
        Account senderAccountFromDb = checkUserExistsInDb(senderId);

        //check if receiver account exists
        Account receiverAccountFromDb = checkUserExistsInDb(receiverId);

        //check if the sender has the given item in his inventory
        if(!senderAccountFromDb.getItems().contains(itemFromDb)){
            throw new IllegalArgumentException("sender doesn't have the given item in his inventory");
        }

        //move the item from sender to receiver
        senderAccountFromDb.getItems().remove(itemFromDb);
        receiverAccountFromDb.getItems().add(itemFromDb);

        _accountContext.save(senderAccountFromDb);
        _accountContext.save(receiverAccountFromDb);
    }

    //region Generic exception methods
    private Account checkUserExistsInDb(int accountId){
        Optional<Account> accountFromDb = _accountContext.findById(accountId);
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("Account with id: " + accountId + " not found in the system");
        }

        return accountFromDb.get();
    }

    private void checkUserMatches(String username, int accountID) {
        int foundAccountID = restCallHelper.getCall(AUTHCALL + username, Account.class).getBody().getAccountID();
        if(foundAccountID != accountID){
            throw new IllegalArgumentException("Account does not have access to change other peoples data!");
        }
    }

    private int getAccountIDFromRestCall() {
        return restCallHelper.getCall(AUTHCALL, Account.class).getBody().getAccountID();
    }

    private Item checkAccountHasItem(String username, int itemID) {
        int accountId = getAccountIDFromRestCall();

        Optional<Item> itemFromDb = _itemContext.findById(itemID);
        if(!itemFromDb.isPresent()){
            throw new IllegalArgumentException("Item with id: " +itemID + " not found in the system");
        }

        Item item = itemFromDb.get();

        for(Account a : item.getAccounts()) {
            if(a.getAccountID() == accountId) {
                return item;
            }
        }

        throw new IllegalArgumentException(username + " Does not have this item");
    }

    private Item checkItemExistsInDb(int itemId){
        Optional<Item> itemFromDb = _itemContext.findById(itemId);
        if(!itemFromDb.isPresent()){
            throw new IllegalArgumentException("Item with id: " + itemId  + " not found in the system");
        }

        return itemFromDb.get();
    }

    private void checkAccountContainsItems(Account account){
        if(account.getItems().isEmpty()){
            throw new IllegalArgumentException("Account with id: " + account.getAccountID() + " doesn't contain any items");
        }
    }
    //endregion
}
