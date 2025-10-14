package com.example.demo.controller;

import com.example.demo.dto.CardDTO;
import com.example.demo.dto.CardOperationDTO;
import com.example.demo.dto.TransferDTO;
import com.example.demo.exception.*;
import com.example.demo.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "The user's API for managing their cards")
public class UserController {
    private final CardService cardService;

    @Operation(summary = "Transfer between user's cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PutMapping("/transfer")
    public ResponseEntity<Void> transferBetweenCurrentUserCards(@AuthenticationPrincipal UserDetails userDetails,
                                                                @RequestBody TransferDTO transferDTO)
            throws CardNotActiveException, CardNotEnoughBalance,
            CardNotFoundException, UnauthorizedCardAccesException, UserNotFoundException {
        cardService.transferBetweenCurrentUserCards(userDetails, transferDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Topping up user's card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PutMapping("/toppingUp")
    public ResponseEntity<CardDTO> toppingUpCard(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody CardOperationDTO cardOperationDTO)
            throws CardNotActiveException, CardNotFoundException,
            UnauthorizedCardAccesException, UserNotFoundException {
        return ResponseEntity.ok(cardService.toppingUpCard(userDetails, cardOperationDTO));
    }

    @Operation(summary = "Withdraw user's card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PutMapping("/withdraw")
    public ResponseEntity<CardDTO> withdrawCard(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody CardOperationDTO cardOperationDTO)
            throws CardNotFoundException, CardNotActiveException,
            CardNotEnoughBalance, UnauthorizedCardAccesException, UserNotFoundException {
        return ResponseEntity.ok(cardService.withdrawCard(userDetails, cardOperationDTO));
    }

    @Operation(summary = "Request to block the user's card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PutMapping("/blockingRequest")
    public ResponseEntity<CardDTO> blockingRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody CardOperationDTO cardOperationDTO)
            throws UnauthorizedCardAccesException, UserNotFoundException {
        return ResponseEntity.ok(cardService.blockingRequest(userDetails, cardOperationDTO));
    }

    @Operation(summary = "Getting all the user's cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/cards")
    public ResponseEntity<Set<CardDTO>> getCurrentUserCards(@AuthenticationPrincipal UserDetails userDetails)
            throws UserNotFoundException {
        return ResponseEntity.ok(cardService.getCurrentUserCards(userDetails));
    }

    @Operation(summary = "Getting a user's card by card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/cards/{cardNumber}")
    public ResponseEntity<CardDTO> getCurrentUserCardByNumber(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable String cardNumber)
            throws UnauthorizedCardAccesException, CardNotFoundException, UserNotFoundException {
        return ResponseEntity.ok(cardService.getCurrentUserCardByNumber(userDetails, cardNumber));
    }
}
