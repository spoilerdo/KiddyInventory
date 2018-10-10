package com.kiddinventory.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "AccountItem",
            joinColumns = {@JoinColumn(name = "AccountID")},
            inverseJoinColumns = {@JoinColumn(name = "ItemID")}
    )
    private Set<Item> items = new HashSet<>();

    public Account() {}

    public int getId() {
        return id;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
