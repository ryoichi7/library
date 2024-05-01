package ru.ryoichi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.ryoichi.dao.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends
        JpaRepository<User, Integer>,
        QuerydslPredicateExecutor<User> {

    Optional<User> findByUsername(String username);
}
