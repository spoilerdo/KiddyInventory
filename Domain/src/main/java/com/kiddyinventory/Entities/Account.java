package com.kiddyinventory.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Account {
    @Id
    private int id;
    private String username;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "AccountItem",
            joinColumns = {@JoinColumn(name = "AccountID")},
            inverseJoinColumns = {@JoinColumn(name = "ItemID")}
    )
    private List<Item> items = new ArrayList<>();

    public Account() {}

    public Account(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public Account(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getUsername() {
        return username;
    }
}
