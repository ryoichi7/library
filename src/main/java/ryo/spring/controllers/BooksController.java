package ryo.spring.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ryo.spring.models.Book;
import ryo.spring.models.Person;
import ryo.spring.services.BooksService;
import ryo.spring.services.PeopleService;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {

        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "-1") int page,
                        @RequestParam(name = "bpp", defaultValue = "2147483647") int booksPerPage,
                        @RequestParam(name = "sort", defaultValue = "false") boolean sorted){
        model.addAttribute("books", booksService.findAll(page, booksPerPage, sorted));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){

        Book book = booksService.findOne(id);
        model.addAttribute("book", book);
        if (book != null && book.getOwner() != null)
            model.addAttribute("person", book.getOwner());
        else {
            model.addAttribute("person", new Person());
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/show";
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(@RequestParam("query") String query, Model model){
        model.addAttribute("books", booksService.findByName(query));
        return "books/search";
    }
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){

        if (bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("{id}/detach")
    public String detach(@PathVariable("id") int id){
        booksService.detach(id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("{id}/attach")
    public String attach(@ModelAttribute("person") Person person, @PathVariable("id") int id){
        booksService.attach(id, person);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }
}
