package com.example.demo.controller;

import com.example.demo.dto.CardDTO;
import com.example.demo.dto.CreateCardDTO;
import com.example.demo.exception.CardAlreadyExists;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping()
    public ResponseEntity<List<CardDTO>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardDTO> getCardByCardNumber(@PathVariable String cardNumber)
            throws CardNotFoundException {
        return ResponseEntity.ok(cardService.getCard(cardNumber));
    }

    @PostMapping("/create/new")
    public ResponseEntity<CardDTO> createCard(@RequestBody CreateCardDTO cardDTO)
            throws UserNotFoundException, CardAlreadyExists {
        return ResponseEntity.ok(cardService.createCard(cardDTO));
    }

    @PostMapping("/{cardNumber}")
    public ResponseEntity<Void> deleteCardByCardNumber(@PathVariable String cardNumber) throws CardNotFoundException {
        cardService.deleteCard(cardNumber);
        return ResponseEntity.noContent().build();
    }
}
