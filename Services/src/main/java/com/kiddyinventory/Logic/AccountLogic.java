package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.LogicInterface.IAccountLogic;
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
}
