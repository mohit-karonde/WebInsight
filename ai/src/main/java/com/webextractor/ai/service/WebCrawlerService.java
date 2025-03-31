package com.webextractor.ai.service;

import com.webextractor.ai.client.ScraperClient;
import com.webextractor.ai.client.GeminiClient;
import com.webextractor.ai.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebCrawlerService {

    private final ScraperClient scraperClient;
    private final GeminiClient geminiClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public WebCrawlerService(ScraperClient scraperClient, GeminiClient geminiClient) {
        this.scraperClient = scraperClient;
        this.geminiClient = geminiClient;
    }

    public ResponseEntity<FinalResponse> processUrl(String url) {
        // Step 1: Get scraped content from Python Scraper API (ye another microservice hai)
        PageSummary pageSummary = scraperClient.scrape(url);

        // Step 2: Format content for Gemini(sundar bana rahe hai)
        String content = formatContent(pageSummary);

        // Step 3: Send content to Gemini
        GeminiRequest request = new GeminiRequest();
        GeminiRequest.Content geminiContent = new GeminiRequest.Content();
        GeminiRequest.Content.Part part = new GeminiRequest.Content.Part();
        part.setText(content);
        geminiContent.setParts(List.of(part));
        request.setContents(List.of(geminiContent));

        GeminiResponse response = geminiClient.generateContent(request);
        String summary = response.getCandidates().get(0).getContent().getParts().get(0).getText();

        // Step 4: JSON response banare hai
//        Map<String, Object> jsonResponse = new HashMap<>();
//        jsonResponse.put("title", pageSummary.getTitle());
//        jsonResponse.put("headings", pageSummary.getHeadings());
//        jsonResponse.put("paragraphs", pageSummary.getParagraphs());
//        jsonResponse.put("geminiSummary", summary);

         ApiResponse apiResponse = new ApiResponse( pageSummary.getTitle(), pageSummary.getHeadings(),pageSummary.getParagraphs(),summary);
        return ResponseEntity.status(HttpStatus.OK).body( new FinalResponse("succefully returned data","200",true,apiResponse));
    }

    private String formatContent(PageSummary pageSummary) {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(pageSummary.getTitle()).append("\n\n");
        sb.append("Headings:\n");
        for (String heading : pageSummary.getHeadings()) {
            sb.append("- ").append(heading).append("\n");
        }
        sb.append("\nContent:\n");
        for (String paragraph : pageSummary.getParagraphs()) {
            sb.append(paragraph).append("\n");
        }
        return sb.toString();
    }
}