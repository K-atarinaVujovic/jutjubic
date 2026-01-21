package com.jutjubiccorps.jutjubic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {
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
        this.validationToken = null;
        this.enabled = false;
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

    @Email
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

    @Column(name = "validationToken")
    @Getter @Setter
    private String validationToken;

    @Column(name = "enabled")
    @Getter @Setter
    private boolean enabled;

    //region UserDetails override

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.emptyList();
    }

    // not important
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    // not important
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    // not important
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return enabled;
    }

    //endregion

    public String generateToken(){
        this.validationToken = UUID.randomUUID().toString();
        return this.validationToken;
    }

    public boolean activateUser(){
        this.enabled = true;
        return this.enabled;
    }
}
