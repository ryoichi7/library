package ryo.spring.services;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryo.spring.models.Book;
import ryo.spring.models.Person;
import ryo.spring.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){
        Person person = peopleRepository.findById(id).orElse(null);
        if (person != null) {
            Hibernate.initialize(person.getBooks());
            for (Book book : person.getBooks()){
                Date currentTime = new Date();
                currentTime.setTime(currentTime.getTime() - (long) 8.64e+8);
                book.setExpired(currentTime.after(book.getTakenAt()));
            }
        }
        return person;
    }

    public Optional<Person> findOne(String name){
        return peopleRepository.findByFullName(name);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }
}
