package ru.ryoichi.service.core;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ryoichi.dao.repository.BookRepository;
import ru.ryoichi.service.exception.DataChangeException;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.*;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("app.book.bucket")
    private final String bucket;
    private final BookRepository bookRepository;

    @SneakyThrows
    public void upload(int bookId, MultipartFile file) {
        if (!bookRepository.existsById(bookId)) {
            throw new DataChangeException("Failed to upload book because no book with id " + bookId + " found");
        }
        var path = Path.of(bucket, String.valueOf(bookId), file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes(), CREATE, TRUNCATE_EXISTING);
    }

    @SneakyThrows
    public ByteArrayInputStream download(int bookId, String path) {
        if (!bookRepository.existsById(bookId)) {
            throw new DataChangeException("Failed to download book file because no book with id " + bookId + " found");
        }
        return new ByteArrayInputStream(Files.readAllBytes(Path.of(bucket, path)));
    }

    public void delete(String path) {

    }
}
