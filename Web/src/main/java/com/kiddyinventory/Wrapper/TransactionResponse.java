package com.kiddyinventory.Wrapper;

public class TransactionResponse {
    int senderId;
    int receiverId;
    int itemId;

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getItemId() {
        return itemId;
    }
}