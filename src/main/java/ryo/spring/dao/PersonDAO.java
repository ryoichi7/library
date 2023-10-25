package ryo.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ryo.spring.models.Person;

import java.util.List;
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
        public Person show(int id){ //READ, Select person with specified id from table
        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?", new Object[]{id}
            , new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
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
