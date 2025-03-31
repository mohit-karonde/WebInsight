package com.webextractor.ai.model;


import java.util.List;

public class ApiResponse {
    private String title;
    private String[] headings;
    private String[] paragraphs;
    private String geminiSummary;

    public ApiResponse(String title, String[] headings, String[] paragraphs, String geminiSummary) {
        this.title = title;
        this.headings = headings;
        this.paragraphs = paragraphs;
        this.geminiSummary = geminiSummary;
    }

    public String getTitle() {
        return title;
    }


    public String[] getHeadings() {
        return headings;
    }

    public String[] getParagraphs() {
        return paragraphs;
    }

    public String getGeminiSummary() {
        return geminiSummary;
    }
}
