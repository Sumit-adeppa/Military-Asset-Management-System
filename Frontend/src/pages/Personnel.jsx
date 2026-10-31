import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Personnel() {
  const { user } = useAuth();
  const [personnel, setPersonnel] = useState([]);
  const [bases, setBases] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name: '', rank: '', serviceNumber: '', baseId: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data));
    }
  }, [user]);

  useEffect(() => {
    api.get('/personnel')
      .then(res => setPersonnel(res.data))
      .catch(err => console.error('Personnel error:', err))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/personnel', form);
      setShowForm(false);
      setForm({ name: '', rank: '', serviceNumber: '', baseId: '' });
      const res = await api.get('/personnel');
      setPersonnel(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error creating personnel');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Personnel</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'Add Personnel'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name</label>
            <input type="text" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Rank</label>
            <input type="text" value={form.rank} onChange={(e) => setForm({ ...form, rank: e.target.value })} />
          </div>
          <div className="form-group">
            <label>Service Number</label>
            <input type="text" value={form.serviceNumber} onChange={(e) => setForm({ ...form, serviceNumber: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Base</label>
            <select value={form.baseId} onChange={(e) => setForm({ ...form, baseId: e.target.value })} required>
              <option value="">Select Base</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
          </div>
          <button type="submit" className="btn btn-primary">Save Personnel</button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Rank</th>
              <th>Service Number</th>
              <th>Base</th>
            </tr>
          </thead>
          <tbody>
            {personnel.map(p => (
              <tr key={p.id}>
                <td>{p.name}</td>
                <td>{p.rank}</td>
                <td>{p.serviceNumber}</td>
                <td>{p.baseName}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
