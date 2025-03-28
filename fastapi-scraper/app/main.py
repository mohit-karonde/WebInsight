from fastapi import FastAPI, HTTPException
from app.scraper import scrape_website
from app.models import PageSummary

app = FastAPI()

@app.get("/")
def root():
    return {"message": "FastAPI Scraper is running"}

@app.get("/scrape")
async def scrape(url: str):
    try:
        result = scrape_website(url)
        return PageSummary(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# To run the app:
# uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
