package ru.ryoichi.service.mapper.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ryoichi.dao.entity.Book;
import ru.ryoichi.service.dto.book.BookDto;
import ru.ryoichi.service.mapper.core.FromEntityMapper;
import ru.ryoichi.service.mapper.core.ToEntityMapper;
import ru.ryoichi.service.mapper.user.UserReadMapper;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class BookMapper implements
        FromEntityMapper<Book, BookDto>,
        ToEntityMapper<BookDto, Book> {

    private final UserReadMapper userReadMapper;

    @Override
    public BookDto mapFrom(Book from) {
        return BookDto.builder()
                .id(from.getId())
                .title(from.getTitle())
                .description(from.getDescription())
                .author(from.getAuthor())
                .visible(from.getVisible())
                .createdAt(from.getCreatedAt())
                .updatedAt(from.getUpdatedAt())
                .user(userReadMapper.mapFrom(from.getUser()))
                .build();
    }

    @Override
    public Book mapTo(BookDto from) {
        var book = Book.builder()
                .id(from.getId())
                .title(from.getTitle())
                .description(from.getDescription())
                .author(from.getAuthor())
                .visible(from.getVisible())
                .build();

        if (from.getId() == null) {
            book.setCreatedAt(Instant.now());
        }
        book.setUpdatedAt(Instant.now());
        return book;
    }
}
