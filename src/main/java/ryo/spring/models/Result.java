package ryo.spring.models;

import java.util.List;

public class Result {
    private final Person person;
    private final Book book;


    public Result(Person person, Book book) {
        this.person = person;
        this.book = book;
    }

    public Person getPerson() {
        return person;
    }

    public Book getBook() {
        return book;
    }
}
