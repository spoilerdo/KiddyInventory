package com.kiddyinventory.Controller;

import com.kiddyinventory.Entities.Account;
import com.kiddyinventory.LogicInterface.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @PostMapping(path = "")
    public ResponseEntity<Account> createAccount() {
        //create account for user with given bank token
        return new ResponseEntity<>(accountLogic.createAccountTest(), HttpStatus.OK);
    }
}
