package com.example.context_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContextService {

    private final FeignConfig.GeminiClient geminiClient;
    private final String apiKey;
    private final String modelName;

    public ContextService(
            FeignConfig.GeminiClient geminiClient,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.model:gemini-2.0-flash}") String modelName) {

        this.geminiClient = geminiClient;
        this.apiKey = apiKey;
        this.modelName = modelName;
    }

    public String generateResponse(String userMessage) {
        Map<String, Object> requestBody = createRequestBody(userMessage);

        Map<String, Object> response = geminiClient.generateContent(modelName, apiKey, requestBody);
        return extractContent(response);
    }

    public String summarizeArticle(String articleText) {
        String prompt = "You are being fed a news article. Your job is to analyze it, extract the core content, identify biases, generate summaries and explain complex topics\nAnswer in the following format:\nTitle:\nPotential Bias:\nSummary of the article\nExplanation of complex topics (if any):\n\n" + articleText;
        Map<String, Object> requestBody = createRequestBody(prompt);

        Map<String, Object> response = geminiClient.generateContent(modelName, apiKey, requestBody);
        return extractContent(response);
    }

    public String getTopic(String articleText) {
        String prompt = "Answer in one word. Give the topic of the article from the following: [Technology, Sports, Politics, Space, Health]" + articleText;
        Map<String, Object> requestBody = createRequestBody(prompt);

        Map<String, Object> response = geminiClient.generateContent(modelName, apiKey, requestBody);
        return extractContent(response);
    }

    private Map<String, Object> createRequestBody(String text) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> part = new HashMap<>();

        part.put("text", text);
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));

        return requestBody;
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
            return "No response generated";
        } catch (Exception e) {
            return "Error extracting response: " + e.getMessage();
        }
    }
}