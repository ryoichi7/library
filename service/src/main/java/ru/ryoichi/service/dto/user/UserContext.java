package ru.ryoichi.service.dto.user;

import lombok.Value;

@Value
public class UserContext {
    Integer userId;
    Boolean isAdmin;
}
