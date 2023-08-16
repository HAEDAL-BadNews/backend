package com.example.news_backend.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_generator")
    @SequenceGenerator(name = "article_generator", sequenceName = "article_seq", allocationSize = 1)
    private Long id;
    private String userId;
    private String title;
    private String context;
    private String url;
    private String author;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_id")
    private Image image;
    private LocalDate article_date;
    private boolean scrap;
    private String category;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> keyword = new HashSet<>();
    private LocalDate now;
    public boolean getScrap() {
        return this.scrap;
    }
}