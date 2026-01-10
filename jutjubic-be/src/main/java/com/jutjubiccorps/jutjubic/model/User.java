package com.jutjubiccorps.jutjubic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
public class User {
    public User() {
        super();
    }

    public User(String firstName, String lastName, String email, String username, String password, String address){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(name = "firstName", nullable = false)
    @Getter @Setter
    private String firstName;

    @Column(name = "lastName", nullable = false)
    @Getter @Setter
    private String lastName;

    @Column(name = "email", unique=true, nullable = false)
    @Getter @Setter
    private String email;

    @Column(name = "username", unique=true, nullable = false)
    @Getter @Setter
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    @Getter @Setter
    private String password;

    @Column(name = "address", nullable = false)
    @Getter @Setter
    private String address;
}
