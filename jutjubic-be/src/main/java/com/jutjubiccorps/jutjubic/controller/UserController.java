package com.jutjubiccorps.jutjubic.controller;

import com.jutjubiccorps.jutjubic.dto.UserDTO;
import com.jutjubiccorps.jutjubic.mapper.UserDTOMapper;
import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.service.UserService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "api/users")
public class UserController {
    private final UserService userService;
    private final UserDTOMapper userMapper;

    public UserController(UserService userService, UserDTOMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(value = "/all")
    @PageableAsQueryParam
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable page){
        Page<User> users = userService.findAll(page);

        Page<UserDTO> usersDTO = users.map(UserDTO::new);

        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping("/whoami")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        User user = userService.findById(id);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
    }
}
