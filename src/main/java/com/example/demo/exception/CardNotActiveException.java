package com.example.demo.exception;

public class CardNotActiveException extends Throwable {
    public CardNotActiveException(String message) {
        super(message);
    }
}
