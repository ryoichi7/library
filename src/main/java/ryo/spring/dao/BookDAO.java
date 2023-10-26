package ryo.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ryo.spring.models.Book;
import ryo.spring.models.Person;
import ryo.spring.models.Result;

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

    public Result show(int id){
        return jdbcTemplate.query("SELECT * FROM Book LEFT JOIN Person ON book.personId = Person.id WHERE book.id = ?",
                new Object[]{id}, (rs, num) -> {
                    Book book = new Book();
                    Person person = new Person();

                    book.setId(rs.getInt(1));
                    book.setPersonId(rs.getInt(2));
                    book.setName(rs.getString(3));
                    book.setAuthor(rs.getString(4));
                    book.setYear(rs.getInt(5));

                    person.setId(rs.getInt(6));
                    person.setFullName(rs.getString(7));
                    person.setDateOfBirth(rs.getString(8));

                    return new Result(person, book);
                }).stream().findAny().orElse(null);
    }

    public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book(personId, name, author, year) VALUES(null, ?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE Book SET name = ?, author = ?, year = ? WHERE id = ?",
                updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void detach(int id){
        jdbcTemplate.update("UPDATE Book SET personId = null WHERE id = ?", id);
    }
    public void attach(int id, int personId){
        jdbcTemplate.update("UPDATE Book SET personId = ? WHERE id = ?", personId, id);
    }
    public void delete(int id){ //DELETE, delete person from table
        jdbcTemplate.update("DELETE FROM Book WHERE id = ?", id);
    }
}
