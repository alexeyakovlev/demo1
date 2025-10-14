package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Represents a card in the application")
public class Card {

    @Schema(description = "Unique ID of the card",
            example = "1",
            required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "A unique 16-digit card number",
            example = "1234567890123456",
            required = true)
    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Schema(description = "Balance on the card",
            example = "1534.15")
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Schema(description = "Status of the card (ACTIVE, BLOCKED or EXPIRED)")
    @Column(nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    @Schema(description = "Date the card was created")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "Card expiration date")
    @Column(nullable = false)
    private LocalDateTime expiredAt = LocalDateTime.now().plusYears(5);

    @Schema(description = "Card owner")
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    @JsonIgnore
    private User user;

    @Schema(description = "Name of card owner")
    @Column(nullable = false)
    private String cardHolder;

    @Schema(description = "Request to block the card")
    @Column(nullable = false)
    private boolean blockingRequest = false;

}
