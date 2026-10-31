import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Users() {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [bases, setBases] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ username: '', password: '', role: 'LOGISTICS_OFFICER', baseId: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data)).catch(() => {});
    }
  }, [user]);

  useEffect(() => {
    api.get('/users')
      .then(res => setUsers(res.data))
      .catch(err => {
        console.error('Users error:', err);
        setError(err.response?.data?.message || 'Failed to load users');
      })
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/users', form);
      setShowForm(false);
      setForm({ username: '', password: '', role: 'LOGISTICS_OFFICER', baseId: '' });
      const res = await api.get('/users');
      setUsers(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error creating user');
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this user?')) return;
    try {
      await api.delete(`/users/${id}`);
      setUsers(users.filter(u => u.id !== id));
    } catch (err) {
      alert(err.response?.data?.message || 'Error deleting user');
    }
  };

  if (user?.role !== 'ADMIN') {
    return <div className="error-message">Access denied. Admin only.</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Users</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'Add User'}
        </button>
      </div>
      {error && <div className="error-message">{error}</div>}
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Username</label>
            <input type="text" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Role</label>
            <select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })} required>
              <option value="ADMIN">Admin</option>
              <option value="BASE_COMMANDER">Base Commander</option>
              <option value="LOGISTICS_OFFICER">Logistics Officer</option>
            </select>
          </div>
          <div className="form-group">
            <label>Base (optional for Admin/Logistics)</label>
            <select value={form.baseId} onChange={(e) => setForm({ ...form, baseId: e.target.value })}>
              <option value="">None</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
          </div>
          <button type="submit" className="btn btn-primary">Save User</button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Role</th>
              <th>Base</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.length === 0 ? (
              <tr><td colSpan="5" style={{ textAlign: 'center' }}>No users found</td></tr>
            ) : (
              users.map(u => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.username}</td>
                  <td>{u.role}</td>
                  <td>{u.baseName || '-'}</td>
                  <td className="actions">
                    <button className="btn btn-sm btn-danger" onClick={() => handleDelete(u.id)}>Delete</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
