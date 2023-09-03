package com.example.news_backend.controller;

import com.example.news_backend.packet.requestbody.ArticleRequestBody;
import com.example.news_backend.packet.requestbody.ImageRequestBody;
import com.example.news_backend.packet.responsebody.ArticleResponseBody;
import com.example.news_backend.packet.responsebody.ImageResponseBody;
import com.example.news_backend.service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    public List<ArticleResponseBody> save_article(@RequestBody ArticleRequestBody requestBody,HttpServletRequest request){
        String url = "http://52.79.232.166:8000/article/save";
        System.out.println("쿠키 출력 시작");
        Cookie[] list = request.getCookies();
        for(Cookie cookie:list) {
            System.out.println(cookie);
        }



        return articleService.call_python(requestBody,url);
    }

    @PostMapping("/home")
    public List<ArticleResponseBody> home_article(@RequestBody ArticleRequestBody requestBody){
        String url = "http://52.79.232.166:8000/article/home";
        return articleService.call_python(requestBody,url);
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
