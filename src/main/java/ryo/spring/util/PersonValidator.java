package ryo.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ryo.spring.dao.PersonDAO;
import ryo.spring.models.Person;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        Optional<Person> old = personDAO.show(person.getFullName());

        if (old.isPresent() && !old.get().getFullName().equals(person.getFullName())){
            errors.rejectValue("fullName", "", "Person with this full name already exists");
        }
    }
}
