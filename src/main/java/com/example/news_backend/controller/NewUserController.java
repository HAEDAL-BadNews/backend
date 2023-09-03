package com.example.news_backend.controller;


import com.example.news_backend.domain.User;
import com.example.news_backend.packet.requestbody.LoginRequestBody;
import com.example.news_backend.packet.responsebody.LoginResponseBody;
import com.example.news_backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/security")
public class NewUserController {
    private UserService userService;

    @Autowired  //
    public NewUserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseBody login(@RequestBody LoginRequestBody requestBody, HttpServletResponse response){
        User user = new User();
        user.setId(requestBody.getId());
        user.setPassword(requestBody.getPassword());


        ResponseCookie cookie = ResponseCookie.from("backCookie", requestBody.getId())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        System.out.print("ok");

        return userService.login(user);
    }

}
