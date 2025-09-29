package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

@Data
public class CreateCardDTO {

    private Long userId;

    private String cardNumber;
}
