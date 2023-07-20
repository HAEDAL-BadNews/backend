package com.example.news_backend.packet.requestbody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestBody {
    private String userId;
    private String category;
    private Integer sort;
}
