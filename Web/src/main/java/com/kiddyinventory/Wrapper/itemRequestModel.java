package com.kiddyinventory.Wrapper;

public class itemRequestModel {

    private int itemID;
    private int accountID;

    public itemRequestModel(int itemID, int accountID) {
        this.itemID = itemID;
        this.accountID = accountID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getAccountID() {
        return accountID;
    }
}
