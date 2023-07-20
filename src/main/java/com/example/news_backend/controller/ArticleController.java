package com.example.news_backend.controller;

import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;
import com.example.news_backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ArticleResponseBody save_article(@RequestBody ArticleRequestBody requestBody){
        return articleService.call_python(requestBody);
    }

}
