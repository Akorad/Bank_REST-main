package com.example.bankcards.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String id) {
        super("Недостаточно средств на карте отправителя " + id);
    }

}
