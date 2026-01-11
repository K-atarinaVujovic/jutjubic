package com.jutjubiccorps.jutjubic.config;

import com.jutjubiccorps.jutjubic.security.auth.RestAuthenticationEntryPoint;
import com.jutjubiccorps.jutjubic.security.auth.TokenAuthenticationFilter;
import com.jutjubiccorps.jutjubic.service.UserService;
import com.jutjubiccorps.jutjubic.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private final UserService userService;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final TokenUtils tokenUtils;

    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, RestAuthenticationEntryPoint restAuthenticationEntryPoint, TokenUtils tokenUtils, BCryptPasswordEncoder passwordEncoder){
        this.userService = userService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.tokenUtils = tokenUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.sessionManagement((session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                // unauthenticated paths here:

                // static resources:
                .requestMatchers(
                "/favicon.ico",
                "/webjars/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/static/**"
                ).permitAll()

                // for any other reqs user must be authenticated:
                .anyRequest().permitAll()
        );

        http.cors(cors -> cors.configure(http));

        http.csrf(csrf -> csrf.disable());

        http.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userService), BasicAuthenticationFilter.class);

        http.authenticationProvider(authenticationProvider());

        return http.build();

    }




}