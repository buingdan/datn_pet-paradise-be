package com.example.petparadisebe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PromotionException extends RuntimeException{
    public PromotionException(String message) {
        super(message);
    }

    public PromotionException(String message, Throwable cause) {
        super(message, cause);
    }
}