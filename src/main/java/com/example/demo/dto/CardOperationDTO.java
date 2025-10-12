package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardOperationDTO {
    String cardNumber;
    BigDecimal amount;
}
