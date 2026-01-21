package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.exception.ConflictException;
import com.jutjubiccorps.jutjubic.exception.NotFoundException;
import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Validation..
        if(userRepository.existsByUsername(user.getUsername())){
            throw new ConflictException("Username " + user.getUsername() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.generateToken();

        return userRepository.save(user);
    }

    public void remove(Long id){
        userRepository.deleteById(id);
    }

    public User findById(Long id){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User " + id + " not found");
        }
        return userRepository.findOneById(id);
    }

    public User findByUsername(String username){
        if(!userRepository.existsByUsername(username)){
            throw new NotFoundException("User " + username + " not found");
        }
        return userRepository.findOneByUsername(username);
    }

    public User findByValidationToken(String token){
        if(!userRepository.existsByValidationToken(token)){
            throw new NotFoundException("User with token " + token + " not found");
        }
        return userRepository.findOneByValidationToken(token);
    }

    public List<User> findAll()
    {
        return userRepository.findAll();
    }


    public Page<User> findAll(Pageable page){
        return userRepository.findAll(page);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void activateUser(User user){
        user.activateUser();
        userRepository.save(user);
    }

    //region UserDetailsService override

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User with email " + email + " not found.");
        }
        return user;
    }

    //endregion
}

