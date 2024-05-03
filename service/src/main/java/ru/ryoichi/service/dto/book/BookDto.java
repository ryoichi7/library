package ru.ryoichi.service.dto.book;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;
import ru.ryoichi.service.dto.user.UserReadDto;

import java.time.Instant;

@Value
@Builder
public class BookDto {
    Integer id;
    String title;
    String description;
    String author;
    Boolean visible;
    Instant createdAt;
    Instant updatedAt;
    UserReadDto user;
}
