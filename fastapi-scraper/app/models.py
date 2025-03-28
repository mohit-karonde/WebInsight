from pydantic import BaseModel
from typing import List

class PageSummary(BaseModel):
    title: str
    headings: List[str]
    paragraphs: List[str]
