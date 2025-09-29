package com.example.demo.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserAlreadyExistsException extends Throwable {
    public UserAlreadyExistsException(@Email @NotBlank String message) {
        super(message);
    }
}
