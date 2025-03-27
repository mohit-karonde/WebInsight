WebInsight is a Spring Boot-based web application that extracts and summarizes content from websites using a web crawler built with Jsoup.
The app allows users to input a website link, and it fetches, cleans, and extracts meaningful text from the webpage.
The extracted content is then processed and summarized using the Gemini API to provide a concise, informative summary.

✅ Optimized for Wikipedia

Efficiently targets Wikipedia pages using #mw-content-text to extract only the main article content.

Handles disambiguation and other non-article pages gracefully.

✅ Web Crawling

Uses Jsoup to scrape website content.

Targets specific HTML elements to extract clean and relevant text.

✅ Content Processing

Cleans and trims the extracted content to remove unnecessary elements.

✅ AI-Based Summarization

Sends extracted content to the Gemini API using WebClient.

The Gemini API returns a structured summary of the content.

✅ Asynchronous Execution

Uses Reactor (Mono/Flux) for non-blocking execution to improve performance.

✅ Structured Output

Returns a PageSummary object containing the webpage title and the summarized content.

Future Enhancements
🚀 Add Selenium and Chrome WebDriver to handle websites with dynamic content (like JavaScript-heavy pages).
🚀 Improve extraction logic to support a wider range of websites beyond Wikipedia.
🚀 Enhance AI prompts to provide more accurate and structured summaries.


