package com.example.client;


import com.example.dto.NewsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "newsClient", url = "${news.api.base-url}")
public interface NewsClient {

    @GetMapping
    NewsResponse getTopHeadlines(@RequestParam("apiKey") String apiKey,
                                 @RequestParam("sources") String sources);
}
