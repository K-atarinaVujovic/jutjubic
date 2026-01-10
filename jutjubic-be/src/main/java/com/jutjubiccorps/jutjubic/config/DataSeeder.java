package com.jutjubiccorps.jutjubic.config;

import com.jutjubiccorps.jutjubic.model.User;
import com.jutjubiccorps.jutjubic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args){
        userService.registerUser(new User("Fato", "Zirosrag", "fato134@yahoo.com", "facini", "fata12345", "Bulevar Vladike Stepe 123"));
        userService.registerUser(new User("Pera", "Peric", "pera@yahoo.com", "pera", "pera", "Perina adresa 3"));
    }
}
