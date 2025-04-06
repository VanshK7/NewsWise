package com.example.context_service.controller;

import com.example.context_service.service.ContextService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm")
public class LlmController {

    private final ContextService contextService;

    public LlmController(ContextService contextService) {
        this.contextService = contextService;
    }

    @PostMapping("/generate")
    public String generateResponse(@RequestBody String prompt) {
        return contextService.generateResponse(prompt);
    }

    @PostMapping("/summarize")
    public String summarizeArticle(@RequestBody String article) {
        return contextService.summarizeArticle(article);
    }
}