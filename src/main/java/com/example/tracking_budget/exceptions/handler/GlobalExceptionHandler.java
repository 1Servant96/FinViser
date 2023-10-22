package com.example.tracking_budget.exceptions.handler;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {TelegramApiException.class, NumberFormatException.class, DataAccessException.class})
    public void handleTelegramApiException(Exception e) {
        System.err.println("Error occurred: " + e.getMessage());
    }
}
