package com.webextractor.ai.model;

public class PageSummary {

    private String title;
    private String[] headings;
    private String[] paragraphs;

    // Constructor
    public PageSummary() {}

    public PageSummary(String title, String[] headings, String[] paragraphs) {
        this.title = title;
        this.headings = headings;
        this.paragraphs = paragraphs;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getHeadings() {
        return headings;
    }

    public void setHeadings(String[] headings) {
        this.headings = headings;
    }

    public String[] getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(String[] paragraphs) {
        this.paragraphs = paragraphs;
    }

    // toString() method
    @Override
    public String toString() {
        return "PageSummary{" +
                "title='" + title + '\'' +
                ", headings=" + String.join(", ", headings) +
                ", paragraphs=" + String.join(", ", paragraphs) +
                '}';
    }
}
