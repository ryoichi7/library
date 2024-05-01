package ru.ryoichi.service.dto.user;

import lombok.Value;

import java.time.Instant;

@Value
public class UserFilter {
    String username;
    Instant createdAt;
    Boolean isCreatedBefore;
    Instant updatedAt;
    Boolean isUpdatedBefore;
}
