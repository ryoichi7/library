package ru.ryoichi.service.exception;

public class UserAccessDeniedException extends RuntimeException{
    public UserAccessDeniedException(String message) {
        super(message);
    }
}
