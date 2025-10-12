package com.example.demo.exception;

public class CardNotEnoughBalance extends Throwable {
    public CardNotEnoughBalance(String message) {
        super(message);
    }
}
