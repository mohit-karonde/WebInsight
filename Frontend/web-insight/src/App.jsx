import { useState } from 'react';
import axios from 'axios';

function App() {
  const [url, setUrl] = useState('');
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!url.trim()) {
      setError('Please enter a valid URL.');
      return;
    }
    setError('');
    setLoading(true);
    setData(null);

    try {
      const response = await axios.post(`http://localhost:8080/api/crawl?url=${encodeURIComponent(url)}`);
      setData(response.data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4">
      <h1 className="text-2xl font-bold mb-4">Web Crawler</h1>
      <form onSubmit={handleSubmit} className="w-full max-w-md">
        <input
          type="text"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="Enter website URL"
          className="w-full p-3 rounded-lg bg-gray-800 border border-gray-700 focus:outline-none focus:border-blue-500 mb-2"
        />
        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg transition duration-200"
          disabled={loading}
        >
          {loading ? 'Crawling...' : 'Submit'}
        </button>
      </form>

      {error && <p className="text-red-500 mt-4">{error}</p>}

      {data && (
        <div className="mt-6 w-full max-w-2xl p-4 bg-gray-800 rounded-lg border border-gray-700">
          <h2 className="text-xl font-semibold mb-2">{data.title || 'No Title'}</h2>
          {data.headings && (
            <div className="mb-2">
              <strong>Headings:</strong>
              <ul className="list-disc pl-5">
                {data.headings.map((heading, index) => (
                  <li key={index}>{heading}</li>
                ))}
              </ul>
            </div>
          )}
          {data.paragraphs && (
            <div>
              <strong>Content:</strong>
              {data.paragraphs.map((para, index) => (
                <p key={index} className="mb-2">{para}</p>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default App;
