package ru.ryoichi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.ryoichi.dao.entity.Book;

@Repository
public interface BookRepository extends
        JpaRepository<Book, Integer>,
        QuerydslPredicateExecutor<Book> {
}
