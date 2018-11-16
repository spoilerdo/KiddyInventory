package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.Helper.RestCallHelper;
import com.kiddyinventory.LogicInterface.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kiddyinventory.Constants.APIConstants.*;

@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository accountContext;
    private RestCallHelper restCall;

    @Autowired
    public AccountLogic(IAccountRepository accountContext, RestCallHelper restCall) {
        this.accountContext = accountContext;
        this.restCall = restCall;
    }

    @Override
    public void createAccount(String username, String password) {
        //check if given username and password exists int the bank-api system
        int bankAccountId = restCall.getCall(GET_BANKACCOUNT + username + "/" + password, int.class).getBody();

        //check if the bankAccount already has a Inventory account
        Optional<Account> account = accountContext.findById(bankAccountId);
        if(account.isPresent()){
            throw new IllegalArgumentException("account with id: " + account.get().getAccountID() + " already exists in the system");
        }

        //create an inventory account
        accountContext.save(new Account(bankAccountId));
    }

    @Override
    public void deleteAccount(int accountId) {
        //check if account exists in the system
        Optional<Account> accountFromDb = accountContext.findById(accountId);
        if(!accountFromDb.isPresent()){
            throw new IllegalArgumentException("account with id: " + accountId + " doesn't exist in the system");
        }

        //delete account
        accountContext.delete(accountFromDb.get());
    }
}
