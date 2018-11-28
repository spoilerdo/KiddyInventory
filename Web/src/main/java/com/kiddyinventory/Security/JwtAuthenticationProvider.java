package com.kiddyinventory.Security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kiddyinventory.Wrapper.LoginWrapper;
import com.kiddyinventory.Wrapper.TokenWrapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.kiddyinventory.Security.SecurityConstants.SecurityConstants.BANK_LOGIN;
import static com.kiddyinventory.Security.SecurityConstants.SecurityConstants.JWTKEY;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsImpl;

    public JwtAuthenticationProvider(UserDetailsService userDetailsImpl) {
        this.userDetailsImpl = userDetailsImpl;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        userDetailsImpl.loadUserByUsername(userDetails.getUsername());
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = retrieveAccountData(new LoginWrapper(username, authentication.getCredentials().toString()));

        Claims claims = Jwts.parser()
                .setSigningKey(JWTKEY)
                .parseClaimsJws(token)
                .getBody();

        List<String> scopes = (List<String>) claims.get("scopes");
        List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        return new User(username, authentication.getCredentials().toString(), authorities);
    }

    private String retrieveAccountData(LoginWrapper loginWrapper){
        URI uri = UriComponentsBuilder.fromUriString(BANK_LOGIN).build().toUri();
        Gson gson = new GsonBuilder().create();

        RequestEntity<String> request = RequestEntity
                .post(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(gson.toJson(loginWrapper));

        //post call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new UsernameNotFoundException(loginWrapper.getUsername());
        }

        //convert to LoginWrapper
        return gson.fromJson(response.getBody(), TokenWrapper.class).getToken();
    }
}
