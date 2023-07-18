package com.example.news_backend.controller;


import com.example.news_backend.domain.User;
import com.example.news_backend.packet.requestbody.LoginRequestBody;
import com.example.news_backend.packet.responsebody.LoginResponseBody;
import com.example.news_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(allowedHeaders = "*")
public class UserController {

    private UserService userSerive;

    @Autowired  //
    public UserController(UserService userSerive){
        this.userSerive = userSerive;
    }

    @PostMapping("/login")
    public LoginResponseBody login(@RequestBody LoginRequestBody requestBody){
        User user = new User();
        user.setId(requestBody.getId());
        user.setPassword(requestBody.getPassword());

        return userSerive.login(user);
    }

}
