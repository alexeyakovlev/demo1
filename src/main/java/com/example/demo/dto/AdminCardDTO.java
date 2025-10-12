package com.example.demo.dto;

import com.example.demo.model.CardStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminCardDTO {
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[0-9]{16}$", message = "The card number must consist of 16 digits")
    private String cardNumber;

    private String cardHolder;

    private BigDecimal balance;

    private CardStatus status;

    private LocalDateTime expiredAt;

    private boolean blockingRequest;
}
