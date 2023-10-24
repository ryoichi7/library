package ryo.spring.models;

import jakarta.validation.constraints.*;

public class Book {

    private int id;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 50,
            message = "Name should be between 2 and 50 characters")
    private String name;

    @NotEmpty(message = "Author's name should not be empty")
    @Size(min = 2, max = 50,
            message = "Author's name should be between 2 and 50 characters")
    private String author;

    @Min(value = 1500, message = "Library does not contain books with less than a 1500 release year")
    @Max(value = 2023, message = "Library does not contain books with greater than a 2023 release year")
    private int year;

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
}
