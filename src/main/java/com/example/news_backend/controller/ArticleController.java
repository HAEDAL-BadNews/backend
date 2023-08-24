package com.example.news_backend.controller;

import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.requestbody.ImageRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;
import com.example.news_backend.packet.responsebody.ImageResponseBody;
import com.example.news_backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/article")
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    @PostMapping("/save")
    public List<ArticleResponseBody> save_article(@RequestBody ArticleRequestBody requestBody){
        return articleService.call_python(requestBody);
    }

    @PostMapping("/scrap")
    public List<ArticleResponseBody> show_scrapedArticle(@RequestBody ArticleRequestBody requestBody) {
        return articleService.show_scrapedArticle(requestBody);
    }


    @PostMapping("/category")
    public List<ArticleResponseBody> recv_image(@RequestBody ArticleRequestBody requestBody){
        return articleService.category_get(requestBody);
    }

    @GetMapping("/scrap/update")
    public ArticleResponseBody change_scrap(@RequestParam Long id){
        return articleService.change_scrap(id);

    }

}
