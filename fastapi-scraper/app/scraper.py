import requests
from bs4 import BeautifulSoup

def scrape_website(url: str):
    headers = {
        "User-Agent": "Mozilla/5.0"
    }
    response = requests.get(url, headers=headers)
    response.raise_for_status()

    soup = BeautifulSoup(response.content, "html.parser")

    # Extract structured data
    title = soup.title.string if soup.title else "No Title Found"
    headings = [h.get_text(strip=True) for h in soup.find_all(["h1", "h2", "h3"])]
    paragraphs = [p.get_text(strip=True) for p in soup.find_all("p")]

    # Return data in a structured format
    return {
        "title": title,
        "headings": headings,
        "paragraphs": paragraphs
    }
