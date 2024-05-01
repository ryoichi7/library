package ru.ryoichi.service.dto.user;

import lombok.Builder;
import lombok.Value;
import ru.ryoichi.dao.entity.Role;

import java.time.Instant;

@Value
@Builder
public class UserReadDto {
    Integer id;
    String username;
    Role role;
    Instant createdAt;
    Instant updatedAt;
}
