package com.jutjubiccorps.jutjubic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JutjubicApplication {

    public static void main(String[] args) {
        SpringApplication.run(JutjubicApplication.class, args);
    }

}
