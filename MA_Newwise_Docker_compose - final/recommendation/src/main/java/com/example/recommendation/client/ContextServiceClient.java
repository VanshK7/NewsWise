//package com.example.recommendation.client;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(name = "context-service", url = "http://localhost:8082")
//public interface ContextServiceClient {
//
//    @PostMapping("/api/llm/extract")
//    String analyzeArticle(@RequestBody String articleText);
//}

package com.example.recommendation.client;

import com.example.recommendation.dto.NewsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "news-service", url = "http://localhost:9090/api/news") // Replace with actual service name & URL
public interface ContextServiceClient {

    @GetMapping("/summarized-news") // Adjust based on the actual endpoint
    NewsResponse getSummarizedNews(@RequestParam String sources);
}
