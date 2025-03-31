from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from bs4 import BeautifulSoup
# it has no use in project just for little testing..
def scrape_website(url: str):
    # Setup Selenium WebDriver (Headless Chrome)
    options = Options()
    options.add_argument("--headless")  # Run in background
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    
    # Properly formatted path
    chrome_driver_path = r"D:\SpringAIwebsiteExplainer\fastapi-scraper\driver\chromedriver-win64\chromedriver.exe"
    service = Service(chrome_driver_path)

    try:
        driver = webdriver.Chrome(service=service, options=options)
        driver.get(url)
        driver.implicitly_wait(5)  # Wait for JavaScript to load
        html = driver.page_source
    except Exception as e:
        print(f"❌ Error loading page: {e}")
        return {"error": str(e)}
    finally:
        driver.quit()

    # Parse with BeautifulSoup
    soup = BeautifulSoup(html, "html.parser")

    # Extract Data
    title = soup.find("h1").text.strip() if soup.find("h1") else "Title not found"
    headings = [h.text.strip() for h in soup.find_all("h2")]
    paragraphs = [p.text.strip() for p in soup.find_all("p")]

    return {
        "title": title,
        "headings": headings,
        "paragraphs": paragraphs
    }
