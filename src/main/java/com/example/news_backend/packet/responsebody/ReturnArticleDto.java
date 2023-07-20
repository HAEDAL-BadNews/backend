package com.example.news_backend.packet.responsebody;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class ReturnArticleDto {
    private String title;
    private String context;
    private String author;
    private String url;
    private String date;
    private String category;
    private String userId;
    //private Set<String> keywords = new HashSet<>();
}
