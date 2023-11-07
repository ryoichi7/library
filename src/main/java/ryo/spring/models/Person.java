package ryo.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;


@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_name")
    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 2, max = 50,
            message = "Full name should be between 2 and 50 characters")
    private String fullName;

    @Column(name = "date_of_birth")
    @Pattern(regexp="(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/(19[0-9][0-9]|20[01][0-9]|202[012])",
            message = "Enter the date in format dd/mm/yyyy")
    private String dateOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(){}
    public Person(int id, String fullName, String dateOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
