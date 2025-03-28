package com.webextractor.ai.controller;


import com.webextractor.ai.model.PageSummary;
import com.webextractor.ai.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/crawl")
@CrossOrigin(origins = "http://localhost:5173")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @PostMapping
    public Mono<PageSummary> crawl(@RequestParam String url) {
        return crawlerService.crawlAndSummarize(url);
    }
}
