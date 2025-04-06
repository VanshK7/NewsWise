package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "contextClient", url = "http://localhost:8089/api/context")
public interface ContextClient {

    @PostMapping(value = "/topic", consumes = "application/json", produces = "application/json")
    Map<String, String> getTopic(@RequestBody String articleText);

    @PostMapping(value = "/summarize", consumes = "application/json", produces = "application/json")
    Map<String, String> summarizeArticle(@RequestBody String articleText);
}




