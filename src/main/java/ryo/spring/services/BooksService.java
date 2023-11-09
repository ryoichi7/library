package ryo.spring.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryo.spring.models.Book;
import ryo.spring.models.Person;
import ryo.spring.repositories.BooksRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sorted){
        if (sorted && page >= 0 && booksPerPage != Integer.MAX_VALUE)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        if (page >= 0 && booksPerPage != Integer.MAX_VALUE)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        if (sorted) return booksRepository.findAll(Sort.by("year"));
        else return booksRepository.findAll();
    }

    public Book findOne(int id){
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> findByName(String pattern){
        if (pattern.isEmpty()) return Collections.emptyList();
        return booksRepository.findByNameContains(pattern);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToUpdate = booksRepository.findById(id).get();
        bookToUpdate.setName(updatedBook.getName());
        bookToUpdate.setAuthor(updatedBook.getAuthor());
        bookToUpdate.setYear(updatedBook.getYear());
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void detach(int id) {

        booksRepository.updateById(null, null, id);
    }

    @Transactional
    public void attach(int id, Person person) {
        booksRepository.updateById(person, new Date(), id);
    }
}
