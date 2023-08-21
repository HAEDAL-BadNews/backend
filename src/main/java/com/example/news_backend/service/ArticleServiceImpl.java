package com.example.news_backend.service;

import com.example.news_backend.domain.Article;
import com.example.news_backend.domain.Image;
import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.requestbody.ImageRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;
import com.example.news_backend.packet.responsebody.ImageResponseBody;
import com.example.news_backend.packet.responsebody.ReturnArticleDto;
import com.example.news_backend.repository.ArticleRepository;
import jakarta.servlet.ServletContext;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    @Autowired
    private ServletContext servletContext;


    @Override
    public ArticleResponseBody save_article(Article article) {
        ArticleResponseBody responseBody = new ArticleResponseBody();

        ImageRequestBody imageRequest = new ImageRequestBody();

        if(!articleRepository.findByUserIdAndTitle(article.getUserId(), article.getTitle()).isPresent()){
            articleRepository.save(article);
        }

        //여기서 이미지 정보 받아오는 함수 호출
        imageRequest.setId(article.getId());
        imageRequest.setContext(article.getContext());

        article.setImage(mapping_image(imageRequest));
        articleRepository.save(article);

        responseBody.setTitle(article.getTitle());
        responseBody.setContext(article.getContext());
        responseBody.setAuthor(article.getAuthor());
        responseBody.setUrl(article.getUrl());
        responseBody.setDate(article.getArticle_date());
        responseBody.setCategory(article.getCategory());
        responseBody.setKeywords(article.getKeyword());
        responseBody.setScrap(article.getScrap());



        return responseBody;
    }

    @Override
    public List<ArticleResponseBody> show_scrapedArticle(ArticleRequestBody requestBody) {

        // findByUserIdAndScrap
        List<Article> scrapedArticles = articleRepository.findAllByUserId(requestBody.getUserId())
                .stream().filter(a -> a.getScrap() == true).toList();

        List<ArticleResponseBody> responseBodies = new ArrayList<>();
        for (int i = 0; i < scrapedArticles.size(); ++i) {
            ArticleResponseBody responseBody = new ArticleResponseBody();

            responseBody.setTitle(scrapedArticles.get(i).getTitle());
            responseBody.setContext(scrapedArticles.get(i).getContext());
            responseBody.setAuthor(scrapedArticles.get(i).getAuthor());
            responseBody.setUrl(scrapedArticles.get(i).getUrl());
            responseBody.setDate(scrapedArticles.get(i).getArticle_date());
            responseBody.setCategory(scrapedArticles.get(i).getCategory());
            responseBody.setKeywords(scrapedArticles.get(i).getKeyword());
            responseBody.setScrap(scrapedArticles.get(i).getScrap());
            responseBody.setImage(scrapedArticles.get(i).getImage());

            responseBodies.add(responseBody);
        }

        return responseBodies;
    }



    @Override
    public ArticleResponseBody call_python(@NotNull ArticleRequestBody requestBody) {
        ArticleResponseBody responseBody;
        ReturnArticleDto articleDto;
        Article article;

        //임시 포트번호 3000
        String url = "http://15.165.122.3:8000/article/save";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",requestBody.getUserId());
        jsonObject.put("category",requestBody.getCategory());
        String jsonString = jsonObject.toString();
        String request = jsonString;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ReturnArticleDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,ReturnArticleDto.class);
        articleDto = responseEntity.getBody();
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
        article.setUrl(articleDto.getUrl());
        article.setArticle_date(LocalDate.parse(articleDto.getDate(), DateTimeFormatter.ISO_DATE));
        article.setCategory(articleDto.getCategory());
        article.setUserId(articleDto.getUserId());
        article.setKeyword(articleDto.getKeywords());
        article.setScrap(false);
        article.setNow(LocalDate.now());
        article.setUserId(articleDto.getUserId());


        return article;
    }


    @ResponseBody
    public Image mapping_image(ImageRequestBody requestBody){
        //python에서 받아와서
        String url = "http://15.165.122.3:8000/article/image";

        ImageResponseBody image_file;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",requestBody.getId());
        jsonObject.put("context",requestBody.getContext());
        String jsonString = jsonObject.toString();
        String request = jsonString;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ImageResponseBody> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ImageResponseBody.class);
        image_file = responseEntity.getBody();

        Image image = new Image();
        image.setId(image_file.getId());
        image.setPath(image_file.getPath());

        //이미지 정보 전달
        return image;
    }


    @Override
    public List<ArticleResponseBody> category_get(ArticleRequestBody requestBody) {
        //userId
        //category
        //sort
        List<Article> scrapedArticles = articleRepository.findAllByCategory(requestBody.getCategory());

        List<ArticleResponseBody> responseBodies = new ArrayList<>();
        for (int i = 0; i < scrapedArticles.size(); ++i) {
            ArticleResponseBody responseBody = new ArticleResponseBody();

            responseBody.setTitle(scrapedArticles.get(i).getTitle());
            responseBody.setContext(scrapedArticles.get(i).getContext());
            responseBody.setAuthor(scrapedArticles.get(i).getAuthor());
            responseBody.setUrl(scrapedArticles.get(i).getUrl());
            responseBody.setDate(scrapedArticles.get(i).getArticle_date());
            responseBody.setCategory(scrapedArticles.get(i).getCategory());
            responseBody.setKeywords(scrapedArticles.get(i).getKeyword());
            responseBody.setScrap(scrapedArticles.get(i).getScrap());
            responseBody.setImage(scrapedArticles.get(i).getImage());

            responseBodies.add(responseBody);
        }

        return responseBodies;

    }

    @Override
    public ArticleResponseBody change_scrap(Long id) {
        Optional<Article> articleOp = articleRepository.findById(id);
        Article article = articleOp.get();
        article.setScrap(!article.getScrap());
        articleRepository.save(article);

        ArticleResponseBody responseBody = new ArticleResponseBody();

        responseBody.setTitle(article.getTitle());
        responseBody.setContext(article.getContext());
        responseBody.setAuthor(article.getAuthor());
        responseBody.setUrl(article.getUrl());
        responseBody.setDate(article.getArticle_date());
        responseBody.setCategory(article.getCategory());
        responseBody.setKeywords(article.getKeyword());
        responseBody.setScrap(article.getScrap());
        responseBody.setImage(article.getImage());



        return responseBody;
    }

}
