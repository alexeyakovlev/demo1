package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private BigDecimal balance =  BigDecimal.ZERO;

    @Column(nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiredAt = LocalDateTime.now().plusYears(5);

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String cardHolder;

    @Column(nullable = false)
    private boolean blockingRequest = false;

}
