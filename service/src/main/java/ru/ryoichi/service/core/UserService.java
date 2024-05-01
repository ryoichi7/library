package ru.ryoichi.service.core;

import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ryoichi.dao.entity.QUser;
import ru.ryoichi.dao.repository.UserRepository;
import ru.ryoichi.service.dto.user.UserCreateEditDto;
import ru.ryoichi.service.dto.user.UserFilter;
import ru.ryoichi.service.dto.user.UserReadDto;
import ru.ryoichi.service.exception.DataChangeException;
import ru.ryoichi.service.exception.DuplicateEntityException;
import ru.ryoichi.service.mapper.user.UserCreateEditMapper;
import ru.ryoichi.service.mapper.user.UserReadMapper;
import ru.ryoichi.service.util.PredicateBuilder;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static ru.ryoichi.dao.entity.QUser.user;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PredicateBuilder predicateBuilder;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var mayBePredicate = predicateBuilder
                .add(filter.getUsername(), user.username::containsIgnoreCase)
                .add(filter.getCreatedAt(),
                        filter.getIsCreatedBefore().equals(Boolean.TRUE) ? user.createdAt::before : user.createdAt::after)
                .add(filter.getUpdatedAt(),
                        filter.getIsUpdatedBefore().equals(Boolean.TRUE) ? user.updatedAt::before : user.updatedAt::after)
                .build();

        var predicate = ofNullable(mayBePredicate).orElseGet(() -> new BooleanBuilder().and(QUser.user.isNotNull()));

        var page = ofNullable(pageable).orElseGet(Pageable::unpaged);

        return userRepository.findAll(predicate, page)
                .map(userReadMapper::mapFrom);
    }

    public UserReadDto findById(Integer id) {
        return userRepository.findById(id)
                .map(userReadMapper::mapFrom)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public UserReadDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userReadMapper::mapFrom)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));
    }

    @Transactional
    public UserReadDto save(UserCreateEditDto userCreateEditDto) {
        if (userCreateEditDto.getId() != null && userRepository.existsById(userCreateEditDto.getId())) {
            throw new DuplicateEntityException("User with id " + userCreateEditDto.getId() + " already exists");
        }
        var user = userCreateEditMapper.mapTo(userCreateEditDto);
        return of(userRepository.save(user))
                .map(userReadMapper::mapFrom)
                .orElseThrow(() -> new DataChangeException("Failed to save user"));
    }

    @Transactional
    public void delete(Integer id) {
        userRepository.findById(id)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new DataChangeException("Failed to delete user with id " + id);
                });
    }
}
