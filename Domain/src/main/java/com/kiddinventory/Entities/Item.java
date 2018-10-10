package com.kiddinventory.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String discription;
    private Condition condition;
    private Float price;

    public enum Condition {FN, MW, FT, WW, BS}

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "items")
    private Set<Account> accounts = new HashSet<>();

    public Item() {}

    public Item(String name, String discription, Condition condition, Float price){
        this.name = name;
        this.discription = discription;
        this.condition = condition;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscription() {
        return discription;
    }

    public Condition getCondition() {
        return condition;
    }

    public Float getPrice() {
        return price;
    }
}
