package com.kiddyinventory.Controller;

import com.kiddyinventory.Entities.Item;
import com.kiddyinventory.LogicInterface.IInventoryLogic;
import com.kiddyinventory.Wrapper.TransactionWrapper;
import com.kiddyinventory.Wrapper.ItemRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private IInventoryLogic inventoryLogic;

    @Autowired
    public InventoryController(IInventoryLogic inventoryLogic) {
        this.inventoryLogic = inventoryLogic;
    }

    @PostMapping(path = "")
    public ResponseEntity<Item> addItemToInventory(Principal user, @RequestBody ItemRequestWrapper itemRequestWrapper) {
        return new ResponseEntity<>(this.inventoryLogic.saveItem(user, itemRequestWrapper.getAccountID(), itemRequestWrapper.getItemID()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemFromInventory(Principal user, @PathVariable("id") int itemID) {
        this.inventoryLogic.deleteItem(user, itemID);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<List<Item>> GetAllItemsFromInventory(Principal user, @PathVariable("id") int accountID) {
        return new ResponseEntity<>(this.inventoryLogic.getItemsFromAccount(user, accountID), HttpStatus.OK);
    }

    @DeleteMapping(path = "/all/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteAllItemsFromInventory(Principal user, @PathVariable("id") int accountID) {
        this.inventoryLogic.deleteItemsFromAccount(user, accountID);
    }

    @PostMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void MoveItem(Principal user, @RequestBody TransactionWrapper response){
        this.inventoryLogic.moveItem(user, response.getSenderId(), response.getReceiverId(), response.getItemId());
    }
}