package com.kiddyinventory.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Account extends ResourceSupport {
    @Id
    private int accountID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AccountItem",
            joinColumns = {@JoinColumn(name = "AccountID")},
            inverseJoinColumns = {@JoinColumn(name = "ItemID")}
    )
    private List<Item> items = new ArrayList<>();

    public Account() {}

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
