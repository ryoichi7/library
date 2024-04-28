package ru.ryoichi.dao.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ryoichi.dao.entity.Role;
import ru.ryoichi.dao.entity.User;
import ru.ryoichi.dao.repository.UserRepository;
import ru.ryoichi.dao.repository.integration.common.IntegrationTestBase;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;
    @Test
    public void checkCreate() {
        var user = User.builder()
                .username("ivan")
                .password("ivanov")
                .role(Role.USER)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        var saved = userRepository.save(user);

        var actual = userRepository.findById(saved.getId());
        Assertions.assertThat(actual).isPresent();
        assertEquals(saved.getId(), actual.get().getId());
    }
}