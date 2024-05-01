package ru.ryoichi.service.exception;

public class DataChangeException extends RuntimeException{
    public DataChangeException(String message) {
        super(message);
    }
}
