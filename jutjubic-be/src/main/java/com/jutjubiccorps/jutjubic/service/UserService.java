package com.jutjubiccorps.jutjubic.service;

import com.jutjubiccorps.jutjubic.exception.ConflictException;
import com.jutjubiccorps.jutjubic.exception.NotFoundException;
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
        if(userRepository.existsByUsername(user.getUsername())){
            throw new ConflictException("Username " + user.getUsername() + " already exists");
        }
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

    public List<User> findAll()
    {
        return userRepository.findAll();
    }


    public Page<User> findAll(Pageable page){
        return userRepository.findAll(page);
    }
}

