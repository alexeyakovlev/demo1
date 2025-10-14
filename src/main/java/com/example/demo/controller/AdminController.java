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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin controller", description = "APIs for managing users and cards")
public class AdminController {

    private final CardService cardService;
    private final UserService userService;

    //manage cards
    @Operation(summary = "Getting all cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/cards")
    public ResponseEntity<Page<AdminCardDTO>>  getAllCards(Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @Operation(summary = "Getting card by card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/cards/number/{cardNumber}")
    public ResponseEntity<AdminCardDTO> getCardByCardNumberForAdmin(@PathVariable String cardNumber)
            throws CardNotFoundException {
        return ResponseEntity.ok(cardService.getCardByCardNumberForAdmin(cardNumber));
    }

    @Operation(summary = "Getting all the user's cards for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/cards/user/{userId}")
    public ResponseEntity<Set<AdminCardDTO>> getUserCardsForAdmin(@PathVariable Long userId)
            throws UserNotFoundException {
        return ResponseEntity.ok(cardService.getUserCardsForAdmin(userId));
    }

    @Operation(summary = "Creating a new card for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PostMapping("/cards/create")
    public ResponseEntity<AdminCardDTO> createCardForUser(@RequestBody CreateCardDTO createCardDTO)
            throws UserNotFoundException, CardAlreadyExists {
        return ResponseEntity.ok(cardService.createCardForUser(createCardDTO));
    }

    @Operation(summary = "Deleting card by card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @DeleteMapping("/cards/delete/{cardNumber}")
    public ResponseEntity<Void> deleteCardByCardNumber(@PathVariable String cardNumber) throws CardNotFoundException {
        cardService.deleteCardByCardNumber(cardNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Blocking or activating card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PutMapping("/cards/block/{cardNumber}")
    public ResponseEntity<AdminCardDTO> blockOrActivateCard(@PathVariable String cardNumber)
            throws CardNotFoundException {
        return ResponseEntity.ok(cardService.blockOrActivateCard(cardNumber));
    }

    @Operation(summary = "Extending validity period of card", description = "passive method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @PutMapping("/cards/extendValidityPeriod/{cardNumber}")
    public ResponseEntity<CardDTO> extendValidityPeriod(@PathVariable String cardNumber) throws CardNotFoundException {
        return ResponseEntity.ok(cardService.extendValidityPeriod(cardNumber));
    }

    @Operation(summary = "Getting all cards with blocking request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/cards/blockingRequest")
    public ResponseEntity<Set<AdminCardDTO>> getCardsWithBlockingRequest() {
        return ResponseEntity.ok(cardService.getCardsWithBlockingRequest());
    }

    //mange users
    @Operation(summary = "Getting all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Getting user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @GetMapping("users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(summary = "Deleting user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful transaction"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid transaction input")
    })
    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
