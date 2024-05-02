package ru.ryoichi.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ryoichi.dao.entity.Book;
import ru.ryoichi.dao.entity.Favourite;
import ru.ryoichi.dao.entity.User;

@Repository
public interface FavouriteRepository extends
        JpaRepository<Favourite, Integer>,
        QuerydslPredicateExecutor<Favourite> {

    int countByUserId(int userId);

    int countByBookId(int bookId);

    @Query("SELECT f.user FROM Favourite f WHERE f.book.id = :bookId")
    Page<User> findUsersByBookId(@Param("bookId") int bookId, Pageable pageable);

    @Query("SELECT f.book FROM Favourite f WHERE f.user.id = :userId")
    Page<Book> findBooksByUserId(@Param("userId") int userId, Pageable pageable);

    boolean existsByUserIdAndBookId(int userId, int bookId);

    @Modifying
    void deleteByUserIdAndBookId(int userId, int bookId);
}
