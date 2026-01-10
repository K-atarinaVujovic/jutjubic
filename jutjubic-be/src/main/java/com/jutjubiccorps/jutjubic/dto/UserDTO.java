package com.jutjubiccorps.jutjubic.dto;

import com.jutjubiccorps.jutjubic.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    public UserDTO(User user){
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getAddress());
    }

    @Getter
    private Long id;

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @Getter
    private String username;

    @Getter
    private String email;

    @Getter
    private String address;

}
