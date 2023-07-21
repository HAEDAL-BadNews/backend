package com.example.news_backend.packet.responsebody;


import com.example.news_backend.domain.Image;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ArticleResponseBody {
    private String title;
    private String context;
    private String author;
    private String url;
    private LocalDate date;
    private String category;
    private Set<String> keywords = new HashSet<>();
    private boolean scrap;
    private Image image;
}
