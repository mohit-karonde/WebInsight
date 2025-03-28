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
            // Fetch and parse content using Jsoup
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .timeout(15_000)
                    .get();

            String title = document.title();

            // Extract content based on the site type
            String content = extractContent(document, url);

            if (content.isEmpty()) {
                throw new RuntimeException("Failed to extract meaningful content from " + url);
            }

            // Send content to Gemini for summarization
            return callGemini(content)
                    .map(explanation -> {
                        PageSummary summary = new PageSummary();
                        summary.setTitle(title);
                        summary.setContentSummary(explanation);
                        return summary;
                    })
                    .block(); // Block only within Mono.fromCallable
        });
    }

    private String extractContent(Document document, String url) {
        String content;
        if (url.contains("wikipedia.org")) {
            content = document.select("#mw-content-text").text().trim();
        } else {
            // Try to select the most meaningful content (article, div, p)
            content = document.select("article, div, p").text().trim();
            if (content.isEmpty()) {
                content = document.body().text().trim(); // Fallback to body content
            }
        }

        // Clean up and sanitize content
        content = cleanContent(content);

        return content;
    }

    private String cleanContent(String content) {
        return content
                .replaceAll("\\[.*?\\]", "") // Remove references like [1], [edit]
                .replaceAll("\\{.*?\\}", "") // Remove {citation needed}
                .replaceAll("\\(.*?\\)", "") // Remove (disambiguation), etc.
                .replaceAll("[\\t\\n\\r]+", " ") // Remove tabs, newlines, and carriage returns
                .replaceAll("[^\\x20-\\x7E]", "") // Remove non-ASCII characters
                .replaceAll("\"", "\\\\\"") // Escape quotes for JSON
                .replaceAll("\\s{2,}", " ") // Remove extra spaces
                .trim();
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
                    try {
                        // Extract response safely
                        Map<String, Object> candidates = (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);
                        Map<String, Object> output = (Map<String, Object>) candidates.get("content");
                        Map<String, Object> part = (Map<String, Object>) ((java.util.List<?>) output.get("parts")).get(0);
                        return (String) part.get("text");
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid response from Gemini API", e);
                    }
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("API Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                    return Mono.error(new RuntimeException("Failed to call Gemini API"));
                });
    }
}
