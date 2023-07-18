package com.example.news_backend.service;

import com.example.news_backend.domain.User;
import com.example.news_backend.packet.responsebody.LoginResponseBody;

public interface UserService {
    LoginResponseBody login(User user);
    LoginResponseBody signup(User user);

}
