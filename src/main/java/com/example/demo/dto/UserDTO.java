package com.example.demo.dto;

import com.example.demo.model.Card;
import com.example.demo.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private Set<Card> cards;
}
