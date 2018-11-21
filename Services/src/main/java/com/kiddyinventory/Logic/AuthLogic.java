package com.kiddyinventory.Logic;

import com.kiddyinventory.DataInterfaces.IAccountRepository;
import com.kiddyinventory.Entities.Account;

import org.assertj.core.util.Strings;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.kiddyinventory.Constants.APIConstants.GET_BANKACCOUNT;
import static java.util.Collections.emptyList;

@Service
public class AuthLogic implements UserDetailsService {
    private HttpServletRequest request;
    private IAccountRepository accountRepository;

    @Autowired
    public AuthLogic(HttpServletRequest request, IAccountRepository context) {
        this.request = request;
        this.accountRepository = context;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetch account from authorization server
        JSONObject account = retrieveAccountData(username);

        //get optional values from json object
        int accountID = account.optInt("id");
        String accountName = account.optString("username");
        String password = account.optString("password");

        //check if values are indeed present
        if(accountID == 0 || Strings.isNullOrEmpty(accountName) || Strings.isNullOrEmpty(password)) {
            throw new UsernameNotFoundException("Something went wrong, please contact support");
        }

        Optional<Account> foundAccount = accountRepository.findById(accountID);

        //check if user has logged in to our inventory API before, if not create new account
        if (!foundAccount.isPresent()) {
            Account newAccount = new Account();
            newAccount.setId(accountID);
            accountRepository.save(newAccount);
        }
        return new User(accountName, password, emptyList());
    }

    private JSONObject retrieveAccountData(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));

        HttpEntity<?> httpEntity = new HttpEntity<>("" , headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GET_BANKACCOUNT + username, HttpMethod.GET, httpEntity, String.class);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new UsernameNotFoundException(username);
        }

        //convert to json
        try {
            JSONObject account = new JSONObject(response.getBody());

            if (account == null) {
                throw new UsernameNotFoundException(username);
            }

            return account;

        } catch(JSONException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
