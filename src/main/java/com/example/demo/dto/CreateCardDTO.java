package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateCardDTO {

    private Long userId;

    @NotBlank
    @Pattern(regexp = "^[0-9]{16}$", message = "The card number must consist of 16 digits")
    private String cardNumber;
}
