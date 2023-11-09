package ryo.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ryo.spring.models.Book;
import ryo.spring.models.Person;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByNameContains(String pattern);
    @Modifying
    @Transactional
    @Query(value = "UPDATE Book b SET b.owner=:owner, b.takenAt=:takenAt WHERE b.id=:id")
    void updateById(@Param("owner") Person owner, @Param("takenAt") Date takenAt, @Param("id") int id);
}
