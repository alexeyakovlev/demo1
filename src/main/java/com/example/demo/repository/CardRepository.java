package com.example.demo.repository;

import com.example.demo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Boolean existsByCardNumber(String cardNumber);

    Card findByCardNumber(String cardNumber);
}
