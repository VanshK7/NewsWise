package com.example.service;

import com.example.client.ContextClient;
import com.example.client.NewsClient;
import com.example.dto.NewsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsClient newsClient;
    private final ContextClient contextClient;

    @Value("${news.api.key}")
    private String apiKey;

    public NewsService(NewsClient newsClient, ContextClient contextClient) {
        this.newsClient = newsClient;
        this.contextClient = contextClient;
    }

    public NewsResponse getCategorizedAndSummarizedNews(String source) {
        NewsResponse newsResponse = newsClient.getTopHeadlines(apiKey, source);

        List<NewsResponse.Article> processedArticles = newsResponse.getArticles().stream()
                .peek(article -> {
                    try {
                        if (article.getContent() != null && !article.getContent().isEmpty()) {
                            // 🛠 Fetch topic and summary from ContextService (Port 8089)
                            Map<String, String> topicResponse = contextClient.getTopic(article.getContent());
                            String topic = topicResponse.getOrDefault("topic", "Unknown");

                            Map<String, String> summaryResponse = contextClient.summarizeArticle(article.getContent());
                            String summary = summaryResponse.getOrDefault("summary", "No summary available");

                            // 🏷️ Set category & summary
                            article.setCategory(topic);
                            article.setContent(summary);
                        } else {
                            article.setCategory("Uncategorized");
                            article.setContent("No content available.");
                        }
                    } catch (Exception e) {
                        article.setCategory("Failed");
                        article.setContent("Error processing article: " + e.getMessage());
                    }
                })
                .collect(Collectors.toList());

        newsResponse.setArticles(processedArticles);
        return newsResponse;
    }
}
