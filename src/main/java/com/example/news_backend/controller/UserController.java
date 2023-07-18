package com.example.news_backend.controller;


import com.example.news_backend.domain.User;
import com.example.news_backend.packet.requestbody.LoginRequestBody;
import com.example.news_backend.packet.responsebody.LoginResponseBody;
import com.example.news_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping
public class UserController {

    private UserService userService;

    @Autowired  //
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseBody login(@RequestBody LoginRequestBody requestBody){
        User user = new User();
        user.setId(requestBody.getId());
        user.setPassword(requestBody.getPassword());

        return userService.login(user);
    }

    @PostMapping("/signup")
    public LoginResponseBody signup(@RequestBody LoginRequestBody requestBody){
        User user = new User();
        user.setId(requestBody.getId());
        user.setPassword(requestBody.getPassword());

        return userService.signup(user);
    }

}
