WebInsight

WebInsight is a Spring Boot-based web application designed to extract and summarize content from websites. Initially, the app used Jsoup for scraping, focusing mainly on Wikipedia content, and summarized the extracted data using the Gemini API.

Key Features:
Initial Setup: The app scraped Wikipedia using Jsoup, targeting the #mw-content-text element, cleaned the content, and sent it to the Gemini API for summarization.

Asynchronous Execution: Non-blocking operations with Reactor (Mono/Flux) improved performance.

Structured Output: A PageSummary object returned the summarized content.

Transition to Microservice Architecture:
To improve scalability, we separated the web scraping functionality into a Python microservice using FastAPI and BeautifulSoup.
