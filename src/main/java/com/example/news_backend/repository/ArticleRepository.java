package com.example.news_backend.repository;

import com.example.news_backend.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByUserIdAndTitle(String userId, String title);

}
