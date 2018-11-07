package com.kiddyinventory.Helper;

import net.minidev.json.JSONValue;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
public class RestCallHelper {
    private HttpServletRequest request;

    @Autowired
    public RestCallHelper(HttpServletRequest request) {
        this.request = request;
    }

    public JSONObject GetAccount(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));

        HttpEntity<?> httpEntity = new HttpEntity<>("" , headers);

        RestTemplate restcall = new RestTemplate();
        ResponseEntity<String> response = restcall.exchange("localhost:8888/account/" + username, HttpMethod.GET, httpEntity, String.class);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        //convert to json
        JSONObject account = (JSONObject) JSONValue.parse(response.getBody());

        return account;

    }


}
