package com.jutjubiccorps.jutjubic.mapper;

import com.jutjubiccorps.jutjubic.dto.CreateUserDTO;
import com.jutjubiccorps.jutjubic.dto.UserDTO;
import com.jutjubiccorps.jutjubic.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {
    private final ModelMapper modelMapper;

    public UserDTOMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    // CreateUserDTO -> User
    public User fromCreateUserDTO(CreateUserDTO dto){
        return modelMapper.map(dto, User.class);
    }
}
