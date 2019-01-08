package com.kiddyinventory.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.Enums.Quality;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Item extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemID;
    private String name;
    private String description;
    private Condition condition;
    private Quality quality;
    private Float price;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<Account> accounts = new HashSet<>();

    public Item() {}

    public Item(String name, String description, Condition condition, Quality quality, Float price){
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.quality = quality;
        this.price = price;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }

    public Quality getQuality() {
        return quality;
    }

    public Float getPrice() {
        return price;
    }
}
