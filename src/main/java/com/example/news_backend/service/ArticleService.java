package com.example.news_backend.service;

import com.example.news_backend.domain.Article;
import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;

import java.util.List;

public interface ArticleService {
    List<ArticleResponseBody> call_python(ArticleRequestBody requestBody, String url);
    List<ArticleResponseBody> save_article(List<Article> article);
    List<ArticleResponseBody> show_scrapedArticle(ArticleRequestBody requestBody);
    List<ArticleResponseBody> category_get(ArticleRequestBody requestBody);
    ArticleResponseBody change_scrap(Long id);
}
