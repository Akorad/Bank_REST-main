package com.example.bankcards.exception;

public class InvalidCardStatusException extends RuntimeException {
    public InvalidCardStatusException(String status) {
        super("Недопустимый статус карты: " + status);
    }
}
