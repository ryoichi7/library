package ru.ryoichi.service.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ryoichi.dao.repository.BookRepository;
import ru.ryoichi.service.mapper.book.BookMapper;
import ru.ryoichi.service.util.PredicateBuilder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final UserService userService;
    private final PredicateBuilder predicateBuilder;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

}
