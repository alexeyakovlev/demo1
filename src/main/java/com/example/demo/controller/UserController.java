package com.example.demo.controller;

import com.example.demo.dto.CardDTO;
import com.example.demo.dto.CardOperationDTO;
import com.example.demo.dto.TransferDTO;
import com.example.demo.exception.*;
import com.example.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final CardService cardService;

    @PutMapping("/transfer")
    public ResponseEntity<Void> transferBetweenCurrentUserCards(@AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestBody TransferDTO transferDTO)
            throws CardNotActiveException, CardNotEnoughBalance,
            CardNotFoundException, UnauthorizedCardAccesException, UserNotFoundException {
        cardService.transferBetweenCurrentUserCards(userDetails, transferDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/toppingUp")
    public ResponseEntity<CardDTO> toppingUpCard(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody CardOperationDTO cardOperationDTO)
            throws CardNotActiveException, CardNotFoundException,
            UnauthorizedCardAccesException, UserNotFoundException {
        return ResponseEntity.ok(cardService.toppingUpCard(userDetails, cardOperationDTO));
    }

    @PutMapping("/withdraw")
    public ResponseEntity<CardDTO> withdrawCard(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody CardOperationDTO cardOperationDTO)
            throws CardNotFoundException, CardNotActiveException,
            CardNotEnoughBalance, UnauthorizedCardAccesException, UserNotFoundException {
        return ResponseEntity.ok(cardService.withdrawCard(userDetails, cardOperationDTO));
    }

    @PutMapping("/blockingRequest")
    public ResponseEntity<CardDTO> blockingRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody CardOperationDTO cardOperationDTO)
            throws UnauthorizedCardAccesException, UserNotFoundException {
        return ResponseEntity.ok(cardService.blockingRequest(userDetails, cardOperationDTO));
    }

    @GetMapping("/cards")
    public ResponseEntity<Set<CardDTO>> getCurrentUserCards(@AuthenticationPrincipal UserDetails userDetails)
            throws UserNotFoundException {
        return ResponseEntity.ok(cardService.getCurrentUserCards(userDetails));
    }

    @GetMapping("/cards/{cardNumber}")
    public ResponseEntity<CardDTO> getCurrentUserCardByNumber(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable String cardNumber)
            throws UnauthorizedCardAccesException, CardNotFoundException, UserNotFoundException {
        return ResponseEntity.ok(cardService.getCurrentUserCardByNumber(userDetails, cardNumber));
    }
}
