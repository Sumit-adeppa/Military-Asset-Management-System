import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Purchases() {
  const { user } = useAuth();
  const [purchases, setPurchases] = useState([]);
  const [bases, setBases] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ baseId: '', equipmentTypeId: '', quantity: '', purchaseDate: '', transactionInfo: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data));
      api.get('/equipment-types').then(res => setEquipmentTypes(res.data));
    }
  }, [user]);

  useEffect(() => {
    api.get('/purchases')
      .then(res => setPurchases(res.data))
      .catch(err => console.error('Purchases error:', err))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/purchases', { ...form, quantity: parseInt(form.quantity) });
      setShowForm(false);
      setForm({ baseId: '', equipmentTypeId: '', quantity: '', purchaseDate: '', transactionInfo: '' });
      const res = await api.get('/purchases');
      setPurchases(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error recording purchase');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Purchases</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'Record Purchase'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Base</label>
            <select value={form.baseId} onChange={(e) => setForm({ ...form, baseId: e.target.value })} required>
              <option value="">Select Base</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Equipment Type</label>
            <select value={form.equipmentTypeId} onChange={(e) => setForm({ ...form, equipmentTypeId: e.target.value })} required>
              <option value="">Select Equipment Type</option>
              {equipmentTypes.map(et => <option key={et.id} value={et.id}>{et.name}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Quantity</label>
            <input type="number" min="1" value={form.quantity} onChange={(e) => setForm({ ...form, quantity: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Purchase Date</label>
            <input type="date" value={form.purchaseDate} onChange={(e) => setForm({ ...form, purchaseDate: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Transaction Info</label>
            <input type="text" value={form.transactionInfo} onChange={(e) => setForm({ ...form, transactionInfo: e.target.value })} />
          </div>
          <button type="submit" className="btn btn-primary">Save Purchase</button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Base</th>
              <th>Equipment</th>
              <th>Quantity</th>
              <th>Transaction Info</th>
              <th>Recorded By</th>
            </tr>
          </thead>
          <tbody>
            {purchases.map(p => (
              <tr key={p.id}>
                <td>{p.purchaseDate}</td>
                <td>{p.baseName}</td>
                <td>{p.equipmentTypeName}</td>
                <td>{p.quantity}</td>
                <td>{p.transactionInfo}</td>
                <td>{p.username}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
