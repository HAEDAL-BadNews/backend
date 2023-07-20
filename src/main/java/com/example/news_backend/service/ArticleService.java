package com.example.news_backend.service;

import com.example.news_backend.domain.Article;
import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;

public interface ArticleService {
    ArticleResponseBody call_python(ArticleRequestBody requestBody);
    ArticleResponseBody save_article(Article article);

}
