package com.example.context_service.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@Configuration
public class FeignConfig {

    @FeignClient(name = "geminiClient", url = "${gemini.api.baseUrl}")
    public interface GeminiClient {
        @PostMapping(value = "/{modelName}:generateContent", consumes = MediaType.APPLICATION_JSON_VALUE)
        Map<String, Object> generateContent(
                @PathVariable("modelName") String modelName,
                @RequestParam("key") String apiKey,
                @RequestBody Map<String, Object> requestBody);
    }
}