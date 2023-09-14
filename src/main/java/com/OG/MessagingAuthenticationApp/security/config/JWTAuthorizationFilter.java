package com.OG.MessagingAuthenticationApp.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import static com.OG.MessagingAuthenticationApp.security.config.SecurityConstants.*;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
       String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)){
            log.info("Header is null");
            chain.doFilter(request,response);
            return;
        }
        log.info("Setting security context");
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request));
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HEADER_STRING);

        log.info("Token String -> {}", token);

        if (token != null){
            String username = JWT.require(Algorithm.HMAC512
                    (SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX,""))
                    .getSubject();
            log.info("Username after detokenizing -> {}", username);

            if(username != null){
                log.info("Returning username password token ->");
                return new UsernamePasswordAuthenticationToken(username,null,new ArrayList<>());
            }
            return null;
        }
        return null;
    }


}
