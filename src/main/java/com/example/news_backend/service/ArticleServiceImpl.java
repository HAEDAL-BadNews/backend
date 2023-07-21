package com.example.news_backend.service;

import com.example.news_backend.domain.Article;
import com.example.news_backend.domain.Image;
import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.requestbody.ImageRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;
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

        //여기서 이미지 정보 받아오는 함수 호출
        imageRequest.setId(1L);
        imageRequest.setContext(article.getContext());

        System.out.print(imageRequest);
        //article.setImage(mapping_image(imageRequest));

        if(!articleRepository.findByUserIdAndTitle(article.getUserId(), article.getTitle()).isPresent()){
            articleRepository.save(article);
        }

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
        String url = "http://127.0.0.1:3000/article/save";
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


        return article;
    }


    @ResponseBody
    public Image mapping_image(ImageRequestBody requestBody){
        //python에서 받아와서
        String url = "http://127.0.0.1:3000/article/image";

        MultipartFile image_file;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",requestBody.getId());
        jsonObject.put("context",requestBody.getContext());
        String jsonString = jsonObject.toString();
        String request = jsonString;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MultipartFile> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, MultipartFile.class);

        Image image = new Image();


        try{
            //./image/{filename}으로 저장
            image_file = responseEntity.getBody();
            String resourceSrc = servletContext.getRealPath("/images");
            String resourceName = image_file.getOriginalFilename();

            image.setId(Long.parseLong(resourceName));
            image.setPath(resourceSrc);

            image_file.transferTo(new File(resourceSrc+resourceName));
        }
        catch (IOException e){
            return null;
        }


        //이미지 정보 전달
        return image;
    }

}
