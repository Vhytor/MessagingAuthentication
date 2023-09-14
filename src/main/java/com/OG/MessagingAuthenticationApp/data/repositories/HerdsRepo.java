package com.OG.MessagingAuthenticationApp.data.repositories;

import com.OG.MessagingAuthenticationApp.data.model.Herds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HerdsRepo extends JpaRepository<Herds,Long> {
    Herds findHerdsByPhoneNumber(String phoneNumber);
}
