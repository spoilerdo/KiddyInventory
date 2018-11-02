package com.kiddyinventory.Controller;

import com.kiddyinventory.LogicInterface.IInventoryLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InventoryController {
    private IInventoryLogic inventoryLogic;

    @Autowired
    public InventoryController(IInventoryLogic inventoryLogic) {
        this.inventoryLogic = inventoryLogic;
    }
}