package ryo.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ryo.spring.models.Book;
import ryo.spring.models.Person;
import ryo.spring.models.Result;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }
    public Optional<Person> show(String fullName){ //READ, Check if person with current fullName exists
        return jdbcTemplate.query("SELECT * FROM Person where fullName = ?",
                new Object[]{fullName}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
        public List<Result> show(int id){ //READ, Select person with specified id from table
        return jdbcTemplate.query("SELECT * FROM Person LEFT JOIN Book ON Person.id = book.personId WHERE Person.id = ?", new Object[]{id},
                (rs, num) -> {
                    Person person = new Person();
                    Book book = new Book();

                    person.setId(rs.getInt(1));
                    person.setFullName(rs.getString(2));
                    person.setDateOfBirth(rs.getString(3));


                    book.setId(rs.getInt(4));
                    book.setPersonId(rs.getInt(5));
                    book.setName(rs.getString(6));
                    book.setAuthor(rs.getString(7));
                    book.setYear(rs.getInt(8));

                return new Result(person, book);
                });
    }

    public void save(Person person){ //CREATE, Insert person in table
        jdbcTemplate.update("INSERT INTO Person(fullName, dateOfBirth) VALUES(?, ?)",
                person.getFullName(), person.getDateOfBirth());
    }
    public void update(int id, Person updatedPerson){ //UPDATE, Update person in table
        jdbcTemplate.update("UPDATE Person SET fullName = ?, dateOfBirth = ? WHERE id = ?",
                updatedPerson.getFullName(), updatedPerson.getDateOfBirth(), id);
    }

    public void delete(int id){ //DELETE, delete person from table
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);
    }
}
