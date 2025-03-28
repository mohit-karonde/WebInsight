import { useState } from 'react';
import axios from 'axios';

function App() {
  const [url, setUrl] = useState('');
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleCrawl = async () => {
    setLoading(true);
    setError(null);
    setSummary(null);

    try {
      const response = await axios.post(`http://localhost:8080/api/crawl?url=${encodeURIComponent(url)}`);
      setSummary(response.data);
    } catch (err) {
      setError('Failed to fetch summary. Please try again.');
    } finally {
      setLoading(false);
    }

    
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4">
      <h1 className="text-3xl font-bold mb-4">WebInsight</h1>
      <div className="w-full max-w-md">
        <input
          type="text"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="Enter website URL"
          className="w-full p-2 rounded-md bg-gray-800 border border-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          onClick={handleCrawl}
          disabled={loading || !url}
          className="mt-3 w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold p-2 rounded-md transition"
        >
          {loading ? 'Fetching...' : 'Extract & Summarize'}
        </button>
      </div>

      {error && <p className="text-red-500 mt-4">{error}</p>}

      {summary && (
        <div className="mt-6 bg-gray-800 p-4 rounded-md shadow-md w-full max-w-md">
          <h2 className="text-xl font-bold mb-2">{summary.title}</h2>
          <p className="text-gray-300 whitespace-pre-wrap">{summary.contentSummary}</p>
        </div>
      )}
    </div>
  );
}

export default App;
