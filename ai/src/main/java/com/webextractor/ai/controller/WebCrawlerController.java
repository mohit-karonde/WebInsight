package com.webextractor.ai.controller;

import com.webextractor.ai.service.WebCrawlerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crawl")
@CrossOrigin(origins = "*") // Allows requests from any origin
public class WebCrawlerController {

    private final WebCrawlerService webCrawlerService;

    public WebCrawlerController(WebCrawlerService webCrawlerService) {
        this.webCrawlerService = webCrawlerService;
    }

    @PostMapping
    public String crawl(@RequestParam String url) {
        return webCrawlerService.processUrl(url);
    }
}
