package com.kiddyinventory.Logic;

import com.fasterxml.jackson.annotation.JsonValue;
import net.minidev.json.JSONValue;
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

import static java.util.Collections.emptyList;

@Service
public class AuthLogic implements UserDetailsService {
    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));

        HttpEntity<?> httpEntity = new HttpEntity<>("" , headers);

        RestTemplate restcall = new RestTemplate();
        ResponseEntity<String> response = restcall.exchange("localhost:8888/account/" + username, HttpMethod.GET, httpEntity, String.class);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new UsernameNotFoundException(username);
        }

        //convert to json
        JSONObject account = (JSONObject)JSONValue.parse(response.getBody());

        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        try {
            User foundUser = new User(account.getString("username"), account.getString("password"), emptyList());
            return foundUser;
        } catch(JSONException e) {
            throw new UsernameNotFoundException(username);
        }
    }
}
