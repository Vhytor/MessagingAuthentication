package com.OG.MessagingAuthenticationApp.security.config;

import com.OG.MessagingAuthenticationApp.data.model.Herds;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static com.OG.MessagingAuthenticationApp.security.config.SecurityConstants.*;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
            throws AuthenticationException {
        try {
            Herds herds = new ObjectMapper().readValue(httpServletRequest.getInputStream(), Herds.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    herds.getPhoneNumber(), herds.getPassWord());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            log.info("Exception Occured -> {}", e.getMessage());
            throw new RuntimeException("");
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse,
                                            FilterChain chain,Authentication authentication) throws IOException{
        String token = JWT.create().withSubject(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(Algorithm.HMAC512(SECRET.getBytes()));
        httpServletResponse.addHeader(HEADER_STRING,TOKEN_PREFIX + token);
    }


}
