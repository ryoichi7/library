package ru.ryoichi.service.mapper.user;

import org.springframework.stereotype.Component;
import ru.ryoichi.dao.entity.User;
import ru.ryoichi.service.dto.user.UserReadDto;
import ru.ryoichi.service.mapper.core.FromEntityMapper;

@Component
public class UserReadMapper implements FromEntityMapper<User, UserReadDto> {
    @Override
    public UserReadDto mapFrom(User from) {
        return UserReadDto.builder()
                .id(from.getId())
                .role(from.getRole())
                .username(from.getUsername())
                .createdAt(from.getCreatedAt())
                .updatedAt(from.getUpdatedAt())
                .build();
    }
}
