import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000, // 30 second timeout
});

// Retry configuration for transient errors
const RETRYABLE_STATUSES = [508, 502, 503, 504];
const MAX_RETRIES = 3;
const RETRY_DELAY = 1000; // 1 second base delay

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const config = error.config;
    
    // Initialize retry count
    if (!config) {
      return Promise.reject(error);
    }
    
    config.__retryCount = config.__retryCount || 0;
    
    const status = error.response?.status;
    const shouldRetry = RETRYABLE_STATUSES.includes(status) && config.__retryCount < MAX_RETRIES;
    
    if (shouldRetry) {
      config.__retryCount += 1;
      const delay = RETRY_DELAY * Math.pow(2, config.__retryCount - 1); // Exponential backoff
      console.warn(`API request failed with ${status}, retrying (${config.__retryCount}/${MAX_RETRIES}) in ${delay}ms...`);
      await sleep(delay);
      return api(config);
    }
    
    // Handle 401 Unauthorized
    if (status === 401) {
      localStorage.removeItem('user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    
    // Enhance error message for 508 errors
    if (status === 508) {
      error.message = 'Server is temporarily unavailable. Please try again in a moment.';
    }
    
    return Promise.reject(error);
  }
);

export default api;
