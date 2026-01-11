package com.jutjubiccorps.jutjubic.security.auth;

import com.jutjubiccorps.jutjubic.util.TokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class TokenAuthenticationFilter extends OncePerRequestFilter {
    final private TokenUtils tokenUtils;

    final private UserDetailsService userDetailsService;

    protected final Log LOGGER = LogFactory.getLog(getClass());

    public TokenAuthenticationFilter(TokenUtils tokenUtils, UserDetailsService userDetailsService){
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    //region OncePerRequestFilter override

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        String username;

        String authToken = tokenUtils.getToken(request);

        try{
            if(authToken != null){
                username = tokenUtils.getUsernameFromToken(authToken);

                if(username != null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if(tokenUtils.validateToken(authToken, userDetails)){
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        catch(ExpiredJwtException ex){
            LOGGER.debug("Token expired!");
        }

        chain.doFilter(request, response);
    }

    //endregion
}
