package ru.ryoichi.service.core;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ryoichi.dao.entity.Book;
import ru.ryoichi.dao.repository.BookRepository;
import ru.ryoichi.dao.repository.UserRepository;
import ru.ryoichi.service.dto.book.BookDto;
import ru.ryoichi.service.dto.book.BookFilter;
import ru.ryoichi.service.dto.book.FileDto;
import ru.ryoichi.service.dto.user.UserContext;
import ru.ryoichi.service.exception.BookAccessDeniedException;
import ru.ryoichi.service.exception.DataChangeException;
import ru.ryoichi.service.mapper.book.BookMapper;
import ru.ryoichi.service.util.PredicateBuilder;

import java.io.ByteArrayInputStream;

import static java.util.Optional.ofNullable;
import static ru.ryoichi.dao.entity.QBook.book;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final FileService fileService;
    private final PredicateBuilder predicateBuilder;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public Page<BookDto> findAll(BookFilter filter, Pageable pageable, UserContext userContext) {

        var mayBePredicate = getMayBePredicate(filter, userContext);
        var predicate = ofNullable(mayBePredicate).orElseGet(() -> new BooleanBuilder().and(book.isNotNull()));
        var page = ofNullable(pageable).orElseGet(Pageable::unpaged);

        return bookRepository.findAll(predicate, page)
                .map(bookMapper::mapFrom);
    }

    public BookDto findById(int id, UserContext userContext) {
        var bookDto = bookRepository.findById(id)
                .map(bookMapper::mapFrom)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        if (bookDto.getVisible() || bookDto.getUser().getId().equals(userContext.getUserId()) || userContext.getIsAdmin()) {
            return bookDto;
        }
        throw new BookAccessDeniedException("Book with id " + id + " can't be reached");
    }

    public ByteArrayInputStream getFileById(int id, UserContext userContext) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        if (book.getVisible() || book.getUser().getId().equals(userContext.getUserId()) || userContext.getIsAdmin()) {
            return fileService.download(book.getId(), book.getPath());
        }
        throw new BookAccessDeniedException("Book with id " + id + " can't be reached");
    }

    public void uploadFile(FileDto fileDto, UserContext userContext) {
        var book = bookRepository.findById(fileDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + fileDto.getId() + " not found"));
        if (book.getVisible() || book.getUser().getId().equals(userContext.getUserId()) || userContext.getIsAdmin()) {
            var path = fileService.upload(fileDto.getId(), fileDto.getFile());
            book.setPath(path);
            bookRepository.save(book);
            return;
        }
        throw new BookAccessDeniedException("Book with id " + fileDto.getId() + " can't be reached");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BookDto create(BookDto bookDto, UserContext userContext) {
        var userId = userContext.getUserId();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        var book = bookMapper.mapTo(bookDto);
        user.addBook(book);

        return bookMapper.mapFrom(bookRepository.save(book));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BookDto update(BookDto bookDto, UserContext userContext) {
        if (!bookRepository.existsById(bookDto.getId())) {
            throw new DataChangeException("Failed to update book");
        }
        if (!bookDto.getUser().getId().equals(userContext.getUserId()) && !userContext.getIsAdmin()) {
            throw new BookAccessDeniedException("Book with id " + bookDto.getId() + " can't be reached");
        }
        var book = bookMapper.mapTo(bookDto);

        return bookMapper.mapFrom(bookRepository.save(book));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(int id, UserContext userContext) {
        bookRepository.findById(id).ifPresentOrElse(
                book -> {
                    if (!book.getUser().getId().equals(userContext.getUserId()) && !userContext.getIsAdmin()) {
                        throw new BookAccessDeniedException("Book with id " + book.getId() + " can't be reached");
                    }
                    fileService.delete(book.getPath());
                    bookRepository.delete(book);
                },
                () -> {
                    throw new DataChangeException("Failed to delete user with id " + id);
                });
    }

    private Predicate getMayBePredicate(BookFilter filter, UserContext userContext) {
        predicateBuilder
                .add(filter.getTitle(), book.title::containsIgnoreCase)
                .add(filter.getDescription(), book.description::containsIgnoreCase)
                .add(filter.getAuthor(), book.author::containsIgnoreCase)
                .add(filter.getCreatedAt(), ofNullable(filter.getIsCreatedBefore())
                        .orElse(false) ? book.createdAt::before : book.createdAt::after)
                .add(filter.getUpdatedAt(), ofNullable(filter.getIsUpdatedBefore())
                        .orElse(false) ? book.updatedAt::before : book.updatedAt::after);

        if (filter.getShowPrivate()) {
            if (!userContext.getIsAdmin()) {
                predicateBuilder.add(book.visible.isTrue().or(book.user.id.eq(userContext.getUserId())));
            }
        } else {
            predicateBuilder.add(book.visible.isTrue());
        }

        return predicateBuilder.build();
    }
}