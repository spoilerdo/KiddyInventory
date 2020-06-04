package com.kiddyinventory.Wrapper;

public class ItemRequestWrapper {
    private int itemID;
    private int accountID;

    public ItemRequestWrapper() {

    }

    public ItemRequestWrapper(int itemID, int accountID) {
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
