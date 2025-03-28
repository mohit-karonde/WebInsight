package com.webextractor.ai.client;

import com.webextractor.ai.model.GeminiRequest;
import com.webextractor.ai.model.GeminiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gemini-client", url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
public interface GeminiClient {

    @PostMapping("?key=${gemini.api.key}")
    GeminiResponse generateContent(@RequestBody GeminiRequest request);
}
