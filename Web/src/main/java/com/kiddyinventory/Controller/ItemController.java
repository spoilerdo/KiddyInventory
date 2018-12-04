package com.kiddyinventory.Controller;

import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.Enums.Condition;
import com.kiddyinventory.LogicInterface.IItemLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {
    private IItemLogic itemLogic;

    @Autowired
    public ItemController(IItemLogic itemLogic) {
        this.itemLogic = itemLogic;
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = itemLogic.createItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        Item updatedItem = itemLogic.updateItem(item);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable("id") int itemID) {
        itemLogic.deleteItem(itemID);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") int itemID) {
        Item foundItem = itemLogic.getItem(itemID);
        return new ResponseEntity<>(foundItem, HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Iterable<Item>> getAllItems() {
        Iterable<Item> items = itemLogic.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping(path = "/conditions")
    public ResponseEntity<List<Condition>> getAllConditions(){
        List<Condition> conditions = itemLogic.getAllConditions();
        return new ResponseEntity<>(conditions, HttpStatus.OK);
    }
}
