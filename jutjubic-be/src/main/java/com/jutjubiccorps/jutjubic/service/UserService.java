package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        // Validation..
        // Hashing password..

        return userRepository.save(user);
    }

    public void remove(Long id){
        userRepository.deleteById(id);
    }

    public User findById(Long id){
        return userRepository.findOneById(id);
    }

    public User findByUsername(String username){
        return userRepository.findOneByUsername(username);
    }

    public List<User> findAll()
    {
        return userRepository.findAll();
    }


    public Page<User> findAll(Pageable page){
        return userRepository.findAll(page);
    }
}

