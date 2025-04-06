package com.example.recommendation.service;

import com.example.recommendation.client.UserServiceClient;
import com.example.recommendation.client.ContextServiceClient;
import com.example.recommendation.dto.NewsResponse;
import com.example.recommendation.dto.NewsResponse.Article;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final UserServiceClient userServiceClient;
    private final ContextServiceClient contextServiceClient;

    public RecommendationService(UserServiceClient userServiceClient, ContextServiceClient contextServiceClient) {
        this.userServiceClient = userServiceClient;
        this.contextServiceClient = contextServiceClient;
    }

    // Fetch the user's preferences from UserService microservice
    public String getUserPreference(Long userId) {
        return userServiceClient.getUserInterest(userId);
    }

    // Get articles from ContextService and filter based on user preferences
    public List<Article> getMatchingArticles(Long userId) {
        // Fetch user preferences
        String userInterest = userServiceClient.getUserInterest(userId);
        if (userInterest == null || userInterest.isEmpty()) {
            System.out.println("User interest not found for userId: " + userId);
            return List.of();
        }
        System.out.println("User Interest: " + userInterest);

        // Fetch summarized articles
        NewsResponse newsResponse = contextServiceClient.getSummarizedNews("bbc-news");
        if (newsResponse == null || newsResponse.getArticles().isEmpty()) {
            System.out.println("No articles received from ContextService.");
            return List.of();
        }

        System.out.println("Fetched articles from ContextService: " + newsResponse.getArticles());

        List<Article> articles = newsResponse.getArticles();
        List<String> preferences = List.of(userInterest.split("\\|"));

        // Log articles before filtering
        articles.forEach(article -> System.out.println("Article Category: " + article.getCategory()));

        // Filter articles
        List<Article> filteredArticles = articles.stream()
                .filter(article -> preferences.stream().anyMatch(article.getCategory()::contains))
                .collect(Collectors.toList());

        System.out.println("Filtered articles: " + filteredArticles);
        return filteredArticles;
    }

}
