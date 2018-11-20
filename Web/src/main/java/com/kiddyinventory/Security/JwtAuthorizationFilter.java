package com.kiddyinventory.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.kiddyinventory.Security.SecurityConstants.SecurityConstants.HEADER_STRING;
import static com.kiddyinventory.Security.SecurityConstants.SecurityConstants.JWTKEY;
import static com.kiddyinventory.Security.SecurityConstants.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
            String token = request.getHeader(HEADER_STRING);
            if (token != null) {
                // make call to bank to

                // parse the token.
                Claims userClaim =  Jwts.parser().setSigningKey(JWTKEY).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();

                //retrieve roles from JWT token and convert to grantedauthority
                List<String> scopes = (List<String>) userClaim.get("scopes");
                List<GrantedAuthority> authorities = scopes.stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());

                if (userClaim.getSubject() != null) {
                    return new UsernamePasswordAuthenticationToken(userClaim.getSubject(), null, authorities);
                }
                return null;
            }
            return null;
    }
}