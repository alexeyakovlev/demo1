package com.example.demo.dto;

import com.example.demo.model.CardStatus;
import com.example.demo.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CardDTO {

    private Long id;

    private String cardNumber;

    private String cardHolder;

    private BigDecimal balance;

    private CardStatus status;

    private LocalDateTime expiredAt;

}
