package ryo.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50,
            message = "Name should be between 2 and 50 characters")
    private String name;


    @Column(name = "author")
    @NotEmpty(message = "Author's name should not be empty")
    @Size(min = 2, max = 50,
            message = "Author's name should be between 2 and 50 characters")
    private String author;

    @Column(name = "year")
    @Min(value = 1500, message = "Library does not contain books with less than a 1500 release year")
    @Max(value = 2023, message = "Library does not contain books with greater than a 2023 release year")
    private int year;


    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;
    public Book(){}
    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
