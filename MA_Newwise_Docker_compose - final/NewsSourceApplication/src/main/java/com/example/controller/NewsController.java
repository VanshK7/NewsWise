package com.example.controller;

import com.example.dto.NewsResponse;
import com.example.service.NewsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/summarized-news")
    public NewsResponse getSummarizedNews(@RequestParam String sources) {
        return newsService.getCategorizedAndSummarizedNews(sources);
    }
}



