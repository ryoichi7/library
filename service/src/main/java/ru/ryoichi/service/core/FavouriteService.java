package ru.ryoichi.service.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ryoichi.dao.entity.Favourite;
import ru.ryoichi.dao.entity.FavouriteId;
import ru.ryoichi.dao.repository.BookRepository;
import ru.ryoichi.dao.repository.FavouriteRepository;
import ru.ryoichi.dao.repository.UserRepository;
import ru.ryoichi.service.dto.book.BookDto;
import ru.ryoichi.service.dto.user.UserContext;
import ru.ryoichi.service.dto.user.UserReadDto;
import ru.ryoichi.service.exception.BookAccessDeniedException;
import ru.ryoichi.service.exception.DataChangeException;
import ru.ryoichi.service.exception.DuplicateEntityException;
import ru.ryoichi.service.mapper.book.BookMapper;
import ru.ryoichi.service.mapper.user.UserReadMapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserReadMapper userReadMapper;
    private final BookMapper bookMapper;

    public int countFavouriteBooksByUserId(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        return favouriteRepository.countByUserId(userId);
    }

    public int countUsersWhoFavouritedBook(int bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book with id " + bookId + " not found");
        }
        return favouriteRepository.countByBookId(bookId);
    }

    public Page<BookDto> findFavouriteBooksByUserId(int userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        return favouriteRepository.findBooksByUserId(userId, pageable)
                .map(bookMapper::mapFrom);
    }

    public Page<UserReadDto> findUsersWhoFavouritedBook(int bookId, Pageable pageable) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book with id " + bookId + " not found");
        }
        return favouriteRepository.findUsersByBookId(bookId, pageable)
                .map(userReadMapper::mapFrom);
    }

    @Transactional
    public void follow(int userId, int bookId, UserContext userContext) {
        if (favouriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new DuplicateEntityException("Book with id " + bookId + " is already being followed by user with id " + userId);
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        if (book.getVisible() || book.getUser().getId().equals(userContext.getUserId()) || userContext.getIsAdmin()) {
            var favourite = new Favourite(new FavouriteId(userId, bookId), user, book);
            favouriteRepository.save(favourite);
            return;
        }
        throw new BookAccessDeniedException("Book with id " + bookId + " can't be reached");
    }

    @Transactional
    public void unfollow(int userId, int bookId) {
        if (!favouriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new DataChangeException("Book with id " + bookId + "is already not being followed by user with id " + userId);
        }
        favouriteRepository.deleteByUserIdAndBookId(userId, bookId);
    }
}
