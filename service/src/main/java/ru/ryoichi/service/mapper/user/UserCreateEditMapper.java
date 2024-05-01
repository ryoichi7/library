package ru.ryoichi.service.mapper.user;

import org.springframework.stereotype.Component;
import ru.ryoichi.dao.entity.User;
import ru.ryoichi.service.dto.user.UserCreateEditDto;
import ru.ryoichi.service.mapper.core.ToEntityMapper;

import java.time.Instant;

@Component
public class UserCreateEditMapper implements ToEntityMapper<UserCreateEditDto, User> {
    @Override
    public User mapTo(UserCreateEditDto from) {
        var user = User.builder()
                .id(from.getId())
                .username(from.getUsername())
                .password(from.getPassword())
                .role(from.getRole())
                .build();
        if (from.getId() == null){
            user.setCreatedAt(Instant.now());
        }
        user.setUpdatedAt(Instant.now());
        return user;
    }
}
