package ru.ryoichi.service.dto.book;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
@Builder
public class FileDto {
    Integer id;
    MultipartFile file;
}
