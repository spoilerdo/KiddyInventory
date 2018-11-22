package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.LogicInterface.IAccountLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository accountRepository;

    @Autowired
    public AccountLogic(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(int accountID) {
        Optional<Account> foundAccount = accountRepository.findById(accountID);

        if(!foundAccount.isPresent()) {
            throw new IllegalArgumentException("Account with id : " + accountID + " Not found");
        }

        return foundAccount.get();
    }

    @Override
    public Account createAccount(Account account) {
        //check if values are not null
        if(Strings.isNullOrEmpty(account.getUsername()) || account.getUsername() == null){
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if account already exists
        Optional<Account> foundAccount = accountRepository.findByUsername(account.getUsername());
        if(foundAccount.isPresent()){
            throw new IllegalArgumentException("Account with username: " + account.getUsername() + " already exists");
        }

        return accountRepository.save(account);
    }
}
