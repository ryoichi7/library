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

    @Value("${app.file.bucket}")
    private final String bucket;
    private final BookRepository bookRepository;

    @SneakyThrows
    public String upload(int bookId, MultipartFile file) {
        if (!bookRepository.existsById(bookId)) {
            throw new DataChangeException("Failed to upload book because no book with id " + bookId + " found");
        }
        var path = "/" + bookId + "/" + file.getOriginalFilename();
        var fullPath = Path.of(bucket, path);
        Files.createDirectories(fullPath.getParent());
        Files.write(fullPath, file.getBytes(), CREATE, TRUNCATE_EXISTING);
        return path;
    }

    @SneakyThrows
    public ByteArrayInputStream download(int bookId, String path) {
        var fullPath = Path.of(bucket, path);
        if (!Files.exists(fullPath)) {
            throw new DataChangeException("Failed to download book file because no book with id " + bookId + " found");
        }
        return new ByteArrayInputStream(Files.readAllBytes(fullPath));
    }

    @SneakyThrows
    public void delete(String path) {
        var fullPath = Path.of(bucket, path);
        if (!Files.exists(fullPath)) {
            throw new DataChangeException("Failed to delete book file because no book on path" + fullPath + " found");
        }
        Files.delete(fullPath);
    }
}
