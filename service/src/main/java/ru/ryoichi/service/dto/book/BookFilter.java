package ru.ryoichi.service.dto.book;

import lombok.Value;

import java.time.Instant;

@Value
public class BookFilter {
    String title;
    String description;
    String author;
    Boolean showPrivate;
    Boolean isCreatedBefore;
    Instant createdAt;
    Boolean isUpdatedBefore;
    Instant updatedAt;
}
