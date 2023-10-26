package ryo.spring.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ryo.spring.dao.BookDAO;
import ryo.spring.dao.PersonDAO;
import ryo.spring.models.Book;
import ryo.spring.models.Person;
import ryo.spring.models.Result;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        Result result = bookDAO.show(id);
        model.addAttribute("book", result.getBook());
        model.addAttribute("person", result.getPerson());
        
        if (result.getBook().getPersonId() == 0)
            model.addAttribute("people", personDAO.index());

        return "books/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDAO.show(id).getBook());
        return "books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){

        if (bindingResult.hasErrors()){
            return "books/edit";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("{id}/detach")
    public String detach(@PathVariable("id") int id){
        bookDAO.detach(id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("{id}/attach")
    public String attach(@ModelAttribute("person") Person person, @PathVariable("id") int id){
        bookDAO.attach(id, person.getId());
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }
}
