package com.jutjubiccorps.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

public class UserTokenState {
    public UserTokenState(){
        this.accessToken = null;
        this.expiresIn = null;
    }

    public UserTokenState(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    @Getter
    @Setter
    private String accessToken;
    @Getter
    @Setter
    private Long expiresIn;
}
