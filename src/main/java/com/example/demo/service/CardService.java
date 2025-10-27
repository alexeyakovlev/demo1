package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.*;
import com.example.demo.model.Card;
import com.example.demo.model.CardStatus;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public AdminCardDTO createCardForUser(CreateCardDTO createCard) throws UserNotFoundException, CardAlreadyExists {
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
        return CardMapper.toAdminCardDTO(savedCard);
    }

    @Transactional
    public void deleteCardByCardNumber(String cardNumber) throws CardNotFoundException {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }

        Card card = cardRepository.findByCardNumber(cardNumber);

        cardRepository.deleteById(card.getId());
    }

    public Page<AdminCardDTO> getAllCards(Pageable pageable) {
        Page<Card> cardsPage = cardRepository.findAll(pageable);
        return cardsPage.map(CardMapper::toAdminCardDTO);
    }

    public Set<CardDTO> getCurrentUserCards(UserDetails userDetails) throws UserNotFoundException {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        return currentUser.getCards().stream()
                .map(CardMapper::toCardDTO)
                .collect(Collectors.toSet());
    }

    public CardDTO getCurrentUserCardByNumber(UserDetails userDetails, String cardNumber)
            throws CardNotFoundException, UnauthorizedCardAccesException, UserNotFoundException {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }
        Card card = cardRepository.findByCardNumber(cardNumber);

        if (!card.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedCardAccesException("You don't have access to this card");
        }
        return CardMapper.toCardDTO(card);
    }

    public AdminCardDTO getCardByCardNumberForAdmin(String cardNumber) throws CardNotFoundException {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }
        Card card = cardRepository.findByCardNumber(cardNumber);
        return CardMapper.toAdminCardDTO(card);
    }

    public Set<AdminCardDTO> getUserCardsForAdmin(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return user.getCards().stream()
                .map(CardMapper::toAdminCardDTO)
                .collect(Collectors.toSet());
    }

    @Transactional
    public AdminCardDTO blockOrActivateCard(String cardNumber) throws CardNotFoundException {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }
        Card card = cardRepository.findByCardNumber(cardNumber);
        if (card.getStatus() == CardStatus.ACTIVE) {
            card.setStatus(CardStatus.BLOCKED);
        } else if (card.getStatus() == CardStatus.BLOCKED) {
            card.setStatus(CardStatus.ACTIVE);
        }
        card.setBlockingRequest(false);
        cardRepository.save(card);
        return CardMapper.toAdminCardDTO(card);
    }

    public boolean isActiveCard(String cardNumber) throws CardNotFoundException {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }

        Card card = cardRepository.findByCardNumber(cardNumber);
        return card.getStatus() != CardStatus.ACTIVE;
    }

    @Transactional
    public void transferBetweenCurrentUserCards(UserDetails userDetails, TransferDTO transferDTO)
            throws CardNotFoundException, CardNotActiveException,
            CardNotEnoughBalance, UnauthorizedCardAccesException, UserNotFoundException {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Set<Card> cards = currentUser.getCards();
        if (!cards.contains(cardRepository.findByCardNumber(transferDTO.getCardOne()))) {
            throw new UnauthorizedCardAccesException("You don't have access to this card");
        }
        Card cardFrom =  cardRepository.findByCardNumber(transferDTO.getCardOne());
        if (!cards.contains(cardRepository.findByCardNumber(transferDTO.getCardTwo()))) {
            throw new UnauthorizedCardAccesException("You don't have access to this card");
        }
        Card cardTo =  cardRepository.findByCardNumber(transferDTO.getCardTwo());
        if (isActiveCard(transferDTO.getCardOne())) {
            throw new CardNotActiveException("Card with cardNumber " + transferDTO.getCardOne() + " is not active");
        }
        if (isActiveCard(transferDTO.getCardTwo())) {
            throw new CardNotActiveException("Card with cardNumber " + transferDTO.getCardTwo() + " is not active");
        }
        if (cardFrom.getBalance().compareTo(transferDTO.getAmount()) < 0) {
            throw new CardNotEnoughBalance("Receiver's card is not enough balance");
        }
        cardFrom.setBalance(cardFrom.getBalance().subtract(transferDTO.getAmount()));
        cardRepository.save(cardFrom);
        cardTo.setBalance(cardTo.getBalance().add(transferDTO.getAmount()));
        cardRepository.save(cardTo);
    }

    @Transactional
    public CardDTO toppingUpCard(UserDetails userDetails, CardOperationDTO cardOperationDTO)
            throws CardNotFoundException, CardNotActiveException,
            UnauthorizedCardAccesException, UserNotFoundException {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Set<Card> cards = currentUser.getCards();
        if (!cards.contains(cardRepository.findByCardNumber(cardOperationDTO.getCardNumber()))) {
            throw new UnauthorizedCardAccesException("You don't have access to this card");
        }
        Card card =  cardRepository.findByCardNumber(cardOperationDTO.getCardNumber());
        if (isActiveCard(cardOperationDTO.getCardNumber())) {
            throw new CardNotActiveException("Card with cardNumber " + cardOperationDTO.getCardNumber() +
                    " is not active");
        }
        card.setBalance(card.getBalance().add(cardOperationDTO.getAmount()));
        cardRepository.save(card);
        return CardMapper.toCardDTO(card);
    }

    @Transactional
    public CardDTO withdrawCard(UserDetails userDetails, CardOperationDTO cardOperationDTO)
            throws CardNotFoundException, CardNotActiveException, CardNotEnoughBalance,
            UnauthorizedCardAccesException, UserNotFoundException {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Set<Card> cards = currentUser.getCards();
        if (!cards.contains(cardRepository.findByCardNumber(cardOperationDTO.getCardNumber()))) {
            throw new UnauthorizedCardAccesException("You don't have access to this card");
        }
        Card card =  cardRepository.findByCardNumber(cardOperationDTO.getCardNumber());
        if (isActiveCard(cardOperationDTO.getCardNumber())) {
            throw new CardNotActiveException("Card with cardNumber " + cardOperationDTO.getCardNumber() +
                    " is not active");
        }
        if (card.getBalance().compareTo(cardOperationDTO.getAmount()) < 0) {
            throw new CardNotEnoughBalance("Card is not enough balance");
        }
        card.setBalance(card.getBalance().subtract(cardOperationDTO.getAmount()));
        cardRepository.save(card);
        return CardMapper.toCardDTO(card);
    }

    @Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    public void periodDailyExpirationCheck() {
        LocalDateTime now = LocalDateTime.now();
        List<Card> cardsToExpire = cardRepository.findCardsForAutoExpiration(now).stream()
                .peek(card -> card.setStatus(CardStatus.EXPIRED))
                .toList();
        cardRepository.saveAll(cardsToExpire);
    }

    @Transactional
    public CardDTO extendValidityPeriod(String cardNumber) throws CardNotFoundException {
        if (!cardRepository.existsByCardNumber(cardNumber)) {
            throw new CardNotFoundException("Card with cardNumber " + cardNumber + " not found");
        }
        Card card = cardRepository.findByCardNumber(cardNumber);
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiredAt(LocalDateTime.now().plusYears(5));
        cardRepository.save(card);
        return CardMapper.toCardDTO(card);
    }

    @Transactional
    public CardDTO blockingRequest(UserDetails userDetails, CardOperationDTO  cardOperationDTO)
            throws UnauthorizedCardAccesException, UserNotFoundException {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Set<Card> cards = currentUser.getCards();
        if (!cards.contains(cardRepository.findByCardNumber(cardOperationDTO.getCardNumber()))) {
            throw new UnauthorizedCardAccesException("You don't have access to this card");
        }
        Card card =  cardRepository.findByCardNumber(cardOperationDTO.getCardNumber());
        card.setBlockingRequest(true);
        cardRepository.save(card);
        return CardMapper.toCardDTO(card);
    }

    public Set<AdminCardDTO> getCardsWithBlockingRequest() {
        return cardRepository.findCardsWithBlockRequest().stream()
                .map(CardMapper::toAdminCardDTO)
                .collect(Collectors.toSet());
    }
}
