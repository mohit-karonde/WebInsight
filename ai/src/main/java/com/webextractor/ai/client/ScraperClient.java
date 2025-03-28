package com.webextractor.ai.client;


import com.webextractor.ai.model.PageSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "scraper-client", url = "http://localhost:8000")
public interface ScraperClient {

    @GetMapping("/scrape")
    PageSummary scrape(@RequestParam("url") String url);
}
