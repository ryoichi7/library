package ru.ryoichi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ryoichi.dao.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
