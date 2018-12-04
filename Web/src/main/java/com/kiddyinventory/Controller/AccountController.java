package com.kiddyinventory.Controller;

import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.LogicInterface.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        Account createdAccount = accountLogic.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") int id) {
        return new ResponseEntity<>(accountLogic.getAccount(id), HttpStatus.OK);
    }
}
