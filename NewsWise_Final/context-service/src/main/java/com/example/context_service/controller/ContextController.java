package com.example.context_service.controller;

import com.example.context_service.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/context")
public class ContextController {

    private final ContextService contextService;

    @Autowired
    public ContextController(ContextService contextService) {
        this.contextService = contextService;
    }

    @PostMapping("/generate")
    public String generateResponse(@RequestBody String message) {
        return contextService.generateResponse(message);
    }

    @PostMapping(value = "/summarize", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> summarizeArticle(@RequestBody String article) {
        String summary = contextService.summarizeArticle(article);
        Map<String, String> response = new HashMap<>();
        response.put("summary", summary);
        return response;
    }

    @PostMapping(value = "/topic", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getTopic(@RequestBody String article) {
        String topic = contextService.getTopic(article);
        Map<String, String> response = new HashMap<>();
        response.put("topic", topic);
        return response;
    }

}