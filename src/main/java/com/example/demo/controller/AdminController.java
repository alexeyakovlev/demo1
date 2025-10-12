package com.example.demo.controller;

import com.example.demo.dto.AdminCardDTO;
import com.example.demo.dto.CardDTO;
import com.example.demo.dto.CreateCardDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.CardAlreadyExists;
import com.example.demo.exception.CardNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.CardService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CardService cardService;
    private final UserService userService;

    //manage cards
    @GetMapping("/cards")
    public ResponseEntity<Page<AdminCardDTO>>  getAllCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @GetMapping("/cards/number/{cardNumber}")
    public ResponseEntity<AdminCardDTO> getCardByCardNumberForAdmin(@PathVariable String cardNumber)
            throws CardNotFoundException {
        return ResponseEntity.ok(cardService.getCardByCardNumberForAdmin(cardNumber));
    }

    @GetMapping("/cards/user/{userId}")
    public ResponseEntity<Set<AdminCardDTO>> getUserCardsForAdmin(@PathVariable Long userId)
            throws UserNotFoundException {
        return ResponseEntity.ok(cardService.getUserCardsForAdmin(userId));
    }

    @PostMapping("/cards/create")
    public ResponseEntity<AdminCardDTO> createCardForUser(@RequestBody CreateCardDTO createCardDTO)
            throws UserNotFoundException, CardAlreadyExists {
        return ResponseEntity.ok(cardService.createCardForUser(createCardDTO));
    }

    @DeleteMapping("/cards/delete/{cardNumber}")
    public ResponseEntity<Void> deleteCardByCardNumber(@PathVariable String cardNumber) throws CardNotFoundException {
        cardService.deleteCardByCardNumber(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cards/block/{cardNumber}")
    public ResponseEntity<AdminCardDTO> blockOrActivateCard(@PathVariable String cardNumber)
            throws CardNotFoundException {
        return ResponseEntity.ok(cardService.blockOrActivateCard(cardNumber));
    }

    @PutMapping("/cards/extendValidityPeriod/{cardNumber}")
    public ResponseEntity<CardDTO> extendValidityPeriod(@PathVariable String cardNumber) throws CardNotFoundException {
        return ResponseEntity.ok(cardService.extendValidityPeriod(cardNumber));
    }

    @GetMapping("/cards/blockingRequest")
    public ResponseEntity<Set<AdminCardDTO>> getCardsWithBlockingRequest() {
        return ResponseEntity.ok(cardService.getCardsWithBlockingRequest());
    }

    //mange users
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
