package com.example.demo.util;

import com.example.demo.dto.AdminCardDTO;
import com.example.demo.dto.CardDTO;
import com.example.demo.model.Card;

public class CardMapper {
    public static CardDTO toCardDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setCardNumber(card.getCardNumber());
        cardDTO.setCardHolder(card.getCardHolder());
        cardDTO.setBalance(card.getBalance());
        cardDTO.setStatus(card.getStatus());
        cardDTO.setExpiredAt(card.getExpiredAt());
        cardDTO.setBlockingRequest(card.isBlockingRequest());
        return cardDTO;
    }

    public static Card toCard(CardDTO cardDTO) {
        Card card = new Card();
        card.setId(cardDTO.getId());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardHolder(cardDTO.getCardHolder());
        card.setBalance(cardDTO.getBalance());
        card.setStatus(cardDTO.getStatus());
        card.setExpiredAt(cardDTO.getExpiredAt());
        card.setBlockingRequest(cardDTO.isBlockingRequest());
        return card;
    }

    public static AdminCardDTO toAdminCardDTO(Card card) {
        AdminCardDTO adminCardDTO = new AdminCardDTO();
        adminCardDTO.setId(card.getId());
        adminCardDTO.setCardNumber(card.getCardNumber());
        adminCardDTO.setCardHolder(card.getCardHolder());
        adminCardDTO.setBalance(card.getBalance());
        adminCardDTO.setStatus(card.getStatus());
        adminCardDTO.setExpiredAt(card.getExpiredAt());
        adminCardDTO.setBlockingRequest(card.isBlockingRequest());
        return adminCardDTO;
    }

    public static Card toAdminCardDTO(AdminCardDTO adminCardDTO) {
        Card card = new Card();
        card.setId(adminCardDTO.getId());
        card.setCardNumber(adminCardDTO.getCardNumber());
        card.setCardHolder(adminCardDTO.getCardHolder());
        card.setBalance(adminCardDTO.getBalance());
        card.setStatus(adminCardDTO.getStatus());
        card.setExpiredAt(adminCardDTO.getExpiredAt());
        card.setBlockingRequest(adminCardDTO.isBlockingRequest());
        return card;
    }
}
