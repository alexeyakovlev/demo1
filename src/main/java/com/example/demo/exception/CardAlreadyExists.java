package com.example.demo.exception;

public class CardAlreadyExists extends Throwable {
    public CardAlreadyExists(String message) {
        super(message);
    }
}
