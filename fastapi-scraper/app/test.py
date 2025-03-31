import os
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options

# ✅ Update this path if needed
chrome_driver_path = "D:/SpringAIwebsiteExplainer/fastapi-scraper/driver/chromedriver-win64/chromedriver.exe"

# 🛠️ Check if ChromeDriver exists
if os.path.exists(chrome_driver_path):
    print("✅ ChromeDriver path is correct!")
else:
    print("❌ ChromeDriver path is incorrect!")

# 🛠️ Test Selenium WebDriver
try:
    options = Options()
    options.add_argument("--headless")  # Run Chrome in headless mode
    service = Service(chrome_driver_path)
    driver = webdriver.Chrome(service=service, options=options)
    
    driver.get("https://www.google.com")
    print("✅ Selenium WebDriver is working!")
    print("Page Title:", driver.title)  # Should print "Google"

    driver.quit()
except Exception as e:
    print("❌ Error:", str(e))
