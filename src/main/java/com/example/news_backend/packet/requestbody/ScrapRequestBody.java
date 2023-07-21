package com.example.news_backend.packet.requestbody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//GET???
public class ScrapRequestBody {
    private Long id;
}
