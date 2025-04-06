package com.example.recommendation.controller;


import com.example.recommendation.dto.NewsResponse.Article;
import com.example.recommendation.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // Get personalized news feed for the user
    @GetMapping("/feed/{userId}")
    public List<Article> getPersonalizedFeed(@PathVariable Long userId) {
        return recommendationService.getMatchingArticles(userId);
    }

    // Get only the user's preferences from UserService
    @GetMapping("/feed/pref/{userId}")
    public String getUserPreferences(@PathVariable Long userId) {
        return recommendationService.getUserPreference(userId);
    }
}
