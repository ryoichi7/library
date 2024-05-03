package ru.ryoichi.service.exception;

public class BookAccessDeniedException extends RuntimeException {
    public BookAccessDeniedException(String message) {
        super(message);
    }
}
