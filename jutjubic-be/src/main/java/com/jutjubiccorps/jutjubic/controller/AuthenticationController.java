package com.jutjubiccorps.jutjubic.controller;

import com.jutjubiccorps.jutjubic.dto.CreateUserDTO;
import com.jutjubiccorps.jutjubic.dto.LoginUserDTO;
import com.jutjubiccorps.jutjubic.dto.UserTokenState;
import com.jutjubiccorps.jutjubic.exception.ConflictException;
import com.jutjubiccorps.jutjubic.mapper.UserDTOMapper;
import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.util.TokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value="/api/auth", produces= MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    final private TokenUtils tokenUtils;

    final private AuthenticationManager authenticationManager;

    final private UserService userService;

    final private UserDTOMapper userDTOMapper;

    public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager, UserService userService, UserDTOMapper userDTOMapper){
        this.tokenUtils = tokenUtils;
                this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody LoginUserDTO authenticationRequest, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    @PostMapping("/register")
    public ResponseEntity<UserTokenState> addUser(@RequestBody CreateUserDTO userRequest, UriComponentsBuilder ucBuilder) {
        boolean existUser = this.userService.existsByUsername(userRequest.getUsername());

        if (existUser) {
            throw new ConflictException("Username " + userRequest.getUsername() + " already exists");
        }

        User fromDto = userDTOMapper.fromCreateUserDTO(userRequest);
        User user = this.userService.registerUser(fromDto);

        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }
}
