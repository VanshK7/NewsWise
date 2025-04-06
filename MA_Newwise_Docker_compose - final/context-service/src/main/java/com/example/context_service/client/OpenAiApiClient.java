package com.example.context_service.client;

import com.openai.models.ChatCompletion; // Correct import
import com.openai.models.ChatCompletionCreateParams; // Correct import
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openai-api", url = "${openai.api.compatibility.endpoint}")
public interface OpenAiApiClient {

    @PostMapping("/chat/completions")
    ChatCompletion createChatCompletion(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ChatCompletionCreateParams params
    );
}