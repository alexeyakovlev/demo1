package com.example.demo.util;

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
        return cardDTO;
    }
}
