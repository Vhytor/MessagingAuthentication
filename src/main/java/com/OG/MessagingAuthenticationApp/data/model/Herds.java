package com.OG.MessagingAuthenticationApp.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Herds {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String passWord;
    @OneToOne
    private Otp otp;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Authority> authorities;
}
