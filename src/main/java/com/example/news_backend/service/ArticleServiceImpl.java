package com.example.news_backend.service;

import com.example.news_backend.domain.Article;
import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;
import com.example.news_backend.packet.responsebody.ReturnArticleDto;
import com.example.news_backend.repository.ArticleRepository;
import com.example.news_backend.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    @Override
    public ArticleResponseBody save_article(Article article) {
        ArticleResponseBody responseBody = new ArticleResponseBody();

        if(!articleRepository.findByUserIdAndTitle(article.getUserId(), article.getTitle()).isPresent()){
            articleRepository.save(article);
        }
        responseBody.setTitle(article.getTitle());
        responseBody.setContext(article.getContext());
        responseBody.setAuthor(article.getAuthor());
        responseBody.setUrl(article.getUrl());
        responseBody.setDate(article.getArticle_date());
        responseBody.setCategory(article.getCategory());
        //responseBody.setKeywords(article.getKeyword());
        responseBody.setScrap(article.getScrap());

        return responseBody;
    }

    @Override
    public ArticleResponseBody call_python(ArticleRequestBody requestBody) {
        ArticleResponseBody responseBody;
        ReturnArticleDto articleDto;
        Article article;
        //임시 포트번호 3000
        String url = "http://127.0.0.1:3000/article/save";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",requestBody.getUserId());
        jsonObject.put("category",requestBody.getCategory());
        String jsonString = jsonObject.toString();
        String request = jsonString;
        System.out.print(request);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ReturnArticleDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,ReturnArticleDto.class);

        System.out.print(responseEntity);
        articleDto = responseEntity.getBody();

        System.out.print(articleDto);
        article = toEntity(articleDto);

        responseBody = save_article(article);
        return responseBody;
    }

    public Article toEntity(ReturnArticleDto articleDto){
        //userId, dated;
        Article article = new Article();

        article.setTitle(articleDto.getTitle());
        article.setContext(articleDto.getContext());
        article.setAuthor(articleDto.getAuthor());
        article.setUrl(article.getUrl());
        article.setArticle_date(LocalDate.parse(articleDto.getDate(), DateTimeFormatter.ISO_DATE));
        article.setCategory(articleDto.getCategory());
        article.setUserId(articleDto.getUserId());
        //article.setKeyword(articleDto.getKeywords());

        article.setScrap(false);
        article.setNow(LocalDate.now());


        return article;
    }

}
