package com.example.news_backend.packet.responsebody;

import com.example.news_backend.domain.Image;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageResponseBody {
    private MultipartFile image;
}
