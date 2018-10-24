package com.kiddyinventory.Controller;

import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.LogicInterface.IItemLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/item")
public class ItemController {
    private IItemLogic itemLogic;

    @Autowired
    public ItemController(IItemLogic itemLogic) {
        this.itemLogic = itemLogic;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = itemLogic.createItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public void updateItem(@RequestBody Item item) {
        itemLogic.updateItem(item);
    }

    @DeleteMapping(path = "/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable("id") int itemID) {
        itemLogic.deleteItem(itemID);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") int itemID) {
        Item foundItem = itemLogic.getItem(itemID);
        return new ResponseEntity<>(foundItem, HttpStatus.OK);
    }
}
