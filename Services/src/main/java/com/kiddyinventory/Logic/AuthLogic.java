package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.Entities.Account;

import com.kiddyinventory.Entities.JwtUser;
import com.kiddyinventory.Principal.JwtUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class AuthLogic {
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(IAccountRepository context) {
        this.accountRepository = context;
    }

    public UserDetails loadUserById(int userId, String username) throws UsernameNotFoundException {
        Optional<Account> foundAccount = accountRepository.findById(userId);

        Account account;
        //check if user has logged in to our inventory API before, if not create new account
        if (!foundAccount.isPresent()) {
            account = accountRepository.save(new Account(userId, username));
        } else {
            account = foundAccount.get();
        }

        return new JwtUserPrincipal(account);
    }
}
