package com.OG.MessagingAuthenticationApp.security.herds;

import com.OG.MessagingAuthenticationApp.data.model.Authority;
import com.OG.MessagingAuthenticationApp.data.model.Herds;
import com.OG.MessagingAuthenticationApp.data.repositories.HerdsRepo;
import com.OG.MessagingAuthenticationApp.exception.HerdsNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HerdsServiceImpl implements UserDetailsService {

    private HerdsRepo herdsRepo;
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws HerdsNotFoundException {
        Herds herds = herdsRepo.findHerdsByPhoneNumber(phoneNumber);
        if (herds == null){
            throw new HerdsNotFoundException("Herds Not Found");
        }
        return new User(herds.getPhoneNumber(),herds.getPassWord(),getAuthorities(herds.getAuthorities()));
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<Authority> authorities){
        return authorities.stream().map(authority -> {
            return new SimpleGrantedAuthority(authority.getAuthority().name());
        }).collect(Collectors.toList());

    }
}
