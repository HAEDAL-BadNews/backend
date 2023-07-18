package com.example.news_backend.packet.requestbody;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestBody {
    String id;
    String password;
}
