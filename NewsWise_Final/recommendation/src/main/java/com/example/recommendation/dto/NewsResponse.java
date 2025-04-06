package com.example.recommendation.dto;



import java.util.List;

public class NewsResponse {
    private String status;
    private int totalResults;
    private List<Article> articles;

    public static class Article {
        private String title;
        private String description;
        private String content;
        private String category; // Topic from LLM
        private String bias; // Bias detected
        private String complexExplanation; // Complex topic explanation

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getBias() { return bias; }
        public void setBias(String bias) { this.bias = bias; }

        public String getComplexExplanation() { return complexExplanation; }
        public void setComplexExplanation(String complexExplanation) { this.complexExplanation = complexExplanation; }
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }

    public List<Article> getArticles() { return articles; }
    public void setArticles(List<Article> articles) { this.articles = articles; }
}
