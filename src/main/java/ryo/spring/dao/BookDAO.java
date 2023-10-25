package ryo.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ryo.spring.models.Book;
import ryo.spring.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        return jdbcTemplate.query("SELECT * FROM Book", new BookMapper());
    }

    public List<Book> index(int personId){
        List<Book> books = jdbcTemplate.query("SELECT * FROM Book where personId = ?",
                new Object[]{personId}, new BeanPropertyRowMapper<>(Book.class));
        if (!books.isEmpty()) return books;
        return null;
    }
    public Book show(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE id = ?",
                new Object[]{id}, new BookMapper()).stream().findAny().orElse(null);
    }

    public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book(personId, name, author, year) VALUES(null, ?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE Book SET name = ?, author = ?, year = ? WHERE id = ?",
                updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id){ //DELETE, delete person from table
        jdbcTemplate.update("DELETE FROM Book WHERE id = ?", id);
    }
}
