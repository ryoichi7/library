package ru.ryoichi.service.exception;

public class MissingDataException extends RuntimeException {
    public MissingDataException(String message) {
        super(message);
    }
}
