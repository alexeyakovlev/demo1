package com.example.demo.repository;

import com.example.demo.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    Boolean existsByCardNumber(String cardNumber);

    Card findByCardNumber(String cardNumber);

    @Query("SELECT c FROM Card c WHERE c.expiredAt < :currentDate AND c.status = com.example.demo.model.CardStatus.ACTIVE")
    List<Card> findCardsForAutoExpiration(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT c FROM Card c WHERE c.blockingRequest = true")
    List<Card> findCardsWithBlockRequest();
}
