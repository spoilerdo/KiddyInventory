package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.Entities.Account;

import com.kiddyinventory.Principal.JwtUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthLogic implements UserDetailsService {
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(IAccountRepository context) {
        this.accountRepository = context;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> foundAccount = accountRepository.findByUsername(username);

        //check if user has logged in to our inventory API before, if not create new account
        if (!foundAccount.isPresent()) {
            Account newAccount = new Account(username);
            accountRepository.save(newAccount);
        }
        return new JwtUserPrincipal(new Account(username));
    }
}
