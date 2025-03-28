package com.webextractor.ai.service;

import com.webextractor.ai.client.ScraperClient;
import com.webextractor.ai.client.GeminiClient;
import com.webextractor.ai.model.PageSummary;
import com.webextractor.ai.model.GeminiRequest;
import com.webextractor.ai.model.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public String processUrl(String url) {
        // Step 1: Get scraped content from Python Scraper API
        PageSummary pageSummary = scraperClient.scrape(url);

        // Step 2: Format content for Gemini
        String content = formatContent(pageSummary);

        // Step 3: Send content to Gemini
        GeminiRequest request = new GeminiRequest();
        GeminiRequest.Content geminiContent = new GeminiRequest.Content();
        GeminiRequest.Content.Part part = new GeminiRequest.Content.Part();
        part.setText(content);
        geminiContent.setParts(List.of(part));
        request.setContents(List.of(geminiContent));

        GeminiResponse response = geminiClient.generateContent(request);


        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
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
