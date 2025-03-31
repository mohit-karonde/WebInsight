import { useState } from "react";
import axios from "axios";

function App() {
  const [url, setUrl] = useState("");
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    if (!url) return;
    setLoading(true);
    setError(null);
    setData(null);
    try {
      const response = await axios.post(`http://localhost:8080/api/crawl?url=${encodeURIComponent(url)}`);
      setData(response.data.data);
    } catch (err) {
      setError("Failed to fetch data");
    }
    setLoading(false);
  };

  return (
    <div className="p-6 max-w-4xl mx-auto space-y-6 bg-gray-100 min-h-screen">
      <h1 className="text-4xl font-extrabold text-center text-blue-700">Web Crawler</h1>
      <div className="flex space-x-2 bg-white p-4 shadow-md rounded-lg">
        <input
          type="text"
          className="border p-3 flex-grow rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
          placeholder="Enter website URL..."
          value={url}
          onChange={(e) => setUrl(e.target.value)}
        />
        <button
          className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-lg font-semibold transition-all"
          onClick={fetchData}
          disabled={loading}
        >
          {loading ? "Fetching..." : "Fetch"}
        </button>
      </div>
      {error && <div className="text-red-600 font-medium text-center">{error}</div>}
      {data && (
        <div className="bg-white p-6 shadow-lg rounded-lg">
          <h2 className="text-3xl font-bold text-blue-800">{data.title}</h2>
          <div className="mt-4">
            <h3 className="text-xl font-semibold text-gray-800 border-b pb-2">Headings:</h3>
            <ul className="list-disc ml-5 text-gray-700">
              {data.headings.map((heading, index) => (
                <li key={index} className="mt-1">{heading}</li>
              ))}
            </ul>
          </div>
          <div className="mt-4">
            <h3 className="text-xl font-semibold text-gray-800 border-b pb-2">Content:</h3>
            {data.paragraphs.map((para, index) => (
              <p key={index} className="text-gray-700 mt-2 leading-relaxed">{para}</p>
            ))}
          </div>
          <div className="mt-4">
            <h3 className="text-xl font-semibold text-gray-800 border-b pb-2">Gemini Summary:</h3>
            <p className="bg-blue-50 p-4 rounded-md text-gray-900 leading-relaxed">{data.geminiSummary}</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
