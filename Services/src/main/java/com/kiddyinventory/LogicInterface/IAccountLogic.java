package com.kiddyinventory.LogicInterface;

import com.kiddyinventory.Entities.Account;

public interface IAccountLogic {
    /**
     * Create account with information give
     * @param username the Bank-API username you want to create an Inventory account for
     * @param password the Bank-API password you want to create an Inventory account for
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if given parameters are already known in the system
     */
    void createAccount(String username, String password);
    /**
     * Delete account with information given
     * @param accountId the id for the account to delete
     * @return nothing if everything goes correct
     * @throws IllegalArgumentException if account was not found
     */
    void deleteAccount(int accountId);
}
