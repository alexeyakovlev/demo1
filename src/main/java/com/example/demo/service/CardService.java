package com.example.demo.service;

import com.example.demo.dto.CardDTO;
import com.example.demo.dto.CreateCardDTO;
import com.example.demo.exception.CardAlreadyExists;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Card;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Transactional
    public CardDTO createCard(CreateCardDTO createCard) throws UserNotFoundException, CardAlreadyExists {
        if (cardRepository.existsByCardNumber(createCard.getCardNumber())) {
            throw new CardAlreadyExists("Card with cardNumber " + createCard.getCardNumber() + " already exists");
        }

        User user = userRepository.findById(createCard.getUserId()).
                orElseThrow(() -> new UserNotFoundException("User with id " + createCard.getUserId() + " not found"));

        Card card = new Card();
        card.setCardNumber(createCard.getCardNumber());
        card.setCardHolder(user.getUsername());
        card.setUser(user);

        user.getCards().add(card);

        Card savedCard = cardRepository.save(card);
        return CardMapper.toCardDTO(savedCard);
    }

    @Transactional
    public void deleteCard(String cardNumber) throws CardNotFoundException {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }

        Card card = cardRepository.findByCardNumber(cardNumber);

        cardRepository.deleteById(card.getId());
    }

    public CardDTO getCard(String cardNumber) throws CardNotFoundException {

        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }

        Card card = cardRepository.findByCardNumber(cardNumber);
        return CardMapper.toCardDTO(card);
    }

    public List<CardDTO> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        List<CardDTO> cardDTOS = new ArrayList<>();
        for (Card card : cards) {
            CardDTO cardDTO = CardMapper.toCardDTO(card);
            cardDTOS.add(cardDTO);
        }
        return cardDTOS;
    }
}
