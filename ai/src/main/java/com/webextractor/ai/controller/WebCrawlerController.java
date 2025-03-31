package com.webextractor.ai.controller;

import com.webextractor.ai.model.ApiResponse;
import com.webextractor.ai.model.FinalResponse;
import com.webextractor.ai.service.WebCrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/crawl")
@CrossOrigin(origins = "*") // Allows requests from any origin
public class WebCrawlerController {

    private final WebCrawlerService webCrawlerService;

    public WebCrawlerController(WebCrawlerService webCrawlerService) {
        this.webCrawlerService = webCrawlerService;
    }

    @PostMapping
    public ResponseEntity<FinalResponse> crawl(@RequestParam String url) {
        return webCrawlerService.processUrl(url);
    }
}
