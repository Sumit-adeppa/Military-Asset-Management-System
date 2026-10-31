import { useState } from 'react';
import api from '../services/api';
import { AuthContext } from './AuthContext';

function readStoredUser() {
  const stored = localStorage.getItem('user');
  if (!stored) return null;

  try {
    const parsed = JSON.parse(stored);
    if (parsed && parsed.role) {
      return parsed;
    }
  } catch {
    // Fall through and clear the invalid value below.
  }

  localStorage.removeItem('user');
  return null;
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readStoredUser);

  const login = async (username, password) => {
    const response = await api.post('/auth/login', { username, password });
    const userData = response.data;
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
    return userData;
  };

  const logout = async () => {
    try {
      await api.post('/auth/logout');
    } catch (err) {
      console.error('Logout error:', err);
    } finally {
      setUser(null);
      localStorage.removeItem('user');
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
