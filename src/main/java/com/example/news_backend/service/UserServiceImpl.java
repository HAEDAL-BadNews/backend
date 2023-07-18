package com.example.news_backend.service;

import com.example.news_backend.domain.User;
import com.example.news_backend.packet.responsebody.LoginResponseBody;
import com.example.news_backend.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponseBody login(User user) {
        LoginResponseBody responseBody = new LoginResponseBody();
        if(!userRepository.findById(user.getId()).isPresent()){
            responseBody.setId(user.getId());
            responseBody.setResult(true);
        }
        else{
            responseBody.setResult(false);
        }

        return responseBody;
    }

    @Override
    public LoginResponseBody signup(User user) {
        LoginResponseBody responseBody = new LoginResponseBody();

        if(!userRepository.findById(user.getId()).isPresent()){
            userRepository.save(user);
            responseBody.setId(user.getId());
            responseBody.setResult(true);
        }
        else{
            responseBody.setResult(false);
        }

        return responseBody;
    }

}
