package com.webextractor.ai.service;

import com.webextractor.ai.model.PageSummary;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class CrawlerService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create();
    }

    public Mono<PageSummary> crawlAndSummarize(String url) {
        return Mono.fromCallable(() -> {
            // 1. Fetch content using Jsoup
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // Imitate a browser
                    .ignoreHttpErrors(true)
                    .timeout(10_000)
                    .get();

            // 2. Extract title and content (more flexible selector)
            String title = document.title();

            // Try to target meaningful content
            String content = document.select("article, div, p").text().trim();
            if (content.isEmpty()) {
                content = document.body().text().trim(); // Fallback to entire body
            }

            System.out.println("Extracted content: " + content);

            if (content.isEmpty()) {
                throw new RuntimeException("Failed to extract meaningful content from " + url);
            }

            // 3. Call Gemini API for summary
            return callGemini(content)
                    .map(explanation -> {
                        PageSummary summary = new PageSummary();
                        summary.setTitle(title);
                        summary.setContentSummary(explanation);
                        return summary;
                    }).block(); // Block only inside Mono.fromCallable
        });
    }


    private String extractContent(Document document) {
        // Extract only the main content section for Wikipedia pages
        String content = document.select("#mw-content-text").text().trim();

        // Clean up and sanitize content
        content = content
                .replaceAll("[\\t\\n\\r]+", " ") // Remove tabs, newlines, and carriage returns
                .replaceAll("[^\\x20-\\x7E]", "") // Remove non-ASCII characters
                .replaceAll("\"", "\\\\\"") // Escape double quotes
                .trim();

        return content;
    }

    private Mono<String> callGemini(String content) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        String requestBody = """
        {
            "contents": [{
                "parts": [{
                    "text": "Please summarize the following webpage content. Focus on the main points and ignore irrelevant details. Provide a clear and concise summary:\\n%s"
                }]
            }]
        }
        """.formatted(content);

        return webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    // Extract content from response
                    Map<String, Object> candidates = (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);
                    Map<String, Object> output = (Map<String, Object>) candidates.get("content");
                    Map<String, Object> part = (Map<String, Object>) ((java.util.List<?>) output.get("parts")).get(0);
                    return (String) part.get("text");
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("API Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Failed to call Gemini API"));
                });
    }

}
