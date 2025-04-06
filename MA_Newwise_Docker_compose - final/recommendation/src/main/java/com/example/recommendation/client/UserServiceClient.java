package com.example.recommendation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081/users") // Change port if needed
public interface UserServiceClient {
    @GetMapping("/{userId}/interest")
    String getUserInterest(@PathVariable Long userId);
}
