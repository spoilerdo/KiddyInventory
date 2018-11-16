package com.kiddyinventory.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.kiddyinventory.Constants.APIConstants.*;

@Service
public class RestCallHelper {
    private HttpServletRequest request;

    @Autowired
    public RestCallHelper(HttpServletRequest request) {
        this.request = request;
    }

    public <T> ResponseEntity<T> getCall(String url, Class<T> type){
        //check if auth header exists
        if(request.getHeader(AUTHHEADER).isEmpty()){
            throw new IllegalArgumentException("No " + AUTHHEADER + " header found");
        }

        //set the requested headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHHEADER, request.getHeader(AUTHHEADER)); //TODO: als de get call niet werkt dan is dit waarschijnlijk fout

        //make a empty entity
        HttpEntity<?> httpEntity = new HttpEntity<>("", headers);

        //get call
        RestTemplate restCall = new RestTemplate();
        ResponseEntity<T> response = restCall.exchange(url, HttpMethod.GET, httpEntity, type);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalArgumentException(response.getBody().toString());
        }

        return response;
    }
}
