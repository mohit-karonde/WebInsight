package com.webextractor.ai.service;

import com.webextractor.ai.client.ScraperClient;
import com.webextractor.ai.model.PageSummary;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {

    private final ScraperClient scraperClient;

    public CrawlerService(ScraperClient scraperClient) {
        this.scraperClient = scraperClient;
    }

    public PageSummary crawlAndSummarize(String url) {
        return scraperClient.scrape(url);
    }
}
