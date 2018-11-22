package com.kiddyinventory.LogicInterface;

import com.kiddyinventory.Entities.Account;

public interface IAccountLogic {
    /**
     * @param accountID the id of the account you want to retrieve
     * @return the found account
     * @throws IllegalArgumentException if account doesn't exists in the db
     */
    Account getAccount(int accountID);
    /**
     * @param account the account you want to create
     * @return the created account
     * @throws IllegalAccessException if account values are not filled in properly
     * @throws IllegalArgumentException if account already exists
     */
    Account createAccount(Account account);
}
