import { useState, useEffect } from 'react';
import api from '../services/api';

export default function Bases() {
  const [bases, setBases] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ name: '', location: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/bases')
      .then(res => setBases(res.data))
      .catch(err => console.error('Bases error:', err))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({ name: '', location: '' });
    setEditingId(null);
    setShowForm(false);
  };

  const handleEdit = (base) => {
    setForm({ name: base.name, location: base.location });
    setEditingId(base.id);
    setShowForm(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.put(`/bases/${editingId}`, form);
      } else {
        await api.post('/bases', form);
      }
      resetForm();
      const res = await api.get('/bases');
      setBases(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error saving base');
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this base?')) return;
    try {
      await api.delete(`/bases/${id}`);
      setBases(bases.filter(b => b.id !== id));
    } catch (err) {
      alert(err.response?.data?.message || 'Error deleting base');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Bases</h1>
        <button className="btn btn-primary" onClick={() => {
          if (showForm && editingId) {
            resetForm();
          } else {
            setShowForm(!showForm);
          }
        }}>
          {showForm ? 'Cancel' : 'Add Base'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name</label>
            <input type="text" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Location</label>
            <input type="text" value={form.location} onChange={(e) => setForm({ ...form, location: e.target.value })} />
          </div>
          <button type="submit" className="btn btn-primary">
            {editingId ? 'Update Base' : 'Save Base'}
          </button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Location</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {bases.map(b => (
              <tr key={b.id}>
                <td>{b.name}</td>
                <td>{b.location}</td>
                <td className="actions">
                  <button className="btn btn-sm btn-warning" onClick={() => handleEdit(b)}>Edit</button>
                  <button className="btn btn-sm btn-danger" onClick={() => handleDelete(b.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
