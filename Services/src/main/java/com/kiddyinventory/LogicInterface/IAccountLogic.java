package com.kiddyinventory.LogicInterface;

import com.kiddyinventory.Entities.Account;

public interface IAccountLogic {
    /**
     * @param accountID the id of the account you want to retrieve
     * @return the found account
     * @throws IllegalArgumentException if account doesn't exists in the db
     */
    Account getAccount(int accountID);
}
