package com.example.demo.util;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setCards(user.getCards());
        return userDTO;
    }
}
