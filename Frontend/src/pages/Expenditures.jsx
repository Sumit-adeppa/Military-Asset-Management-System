import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Expenditures() {
  const { user } = useAuth();
  const [expenditures, setExpenditures] = useState([]);
  const [bases, setBases] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ equipmentTypeId: '', baseId: '', quantity: '', expenditureDate: '', reason: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data));
      api.get('/equipment-types').then(res => setEquipmentTypes(res.data));
    }
  }, [user]);

  useEffect(() => {
    api.get('/expenditures')
      .then(res => setExpenditures(res.data))
      .catch(err => console.error('Expenditures error:', err))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/expenditures', { ...form, quantity: parseInt(form.quantity) });
      setShowForm(false);
      setForm({ equipmentTypeId: '', baseId: '', quantity: '', expenditureDate: '', reason: '' });
      const res = await api.get('/expenditures');
      setExpenditures(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error recording expenditure');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Expenditures</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'Record Expenditure'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Equipment Type</label>
            <select value={form.equipmentTypeId} onChange={(e) => setForm({ ...form, equipmentTypeId: e.target.value })} required>
              <option value="">Select Equipment Type</option>
              {equipmentTypes.map(et => <option key={et.id} value={et.id}>{et.name}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Base</label>
            <select value={form.baseId} onChange={(e) => setForm({ ...form, baseId: e.target.value })} required>
              <option value="">Select Base</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Quantity</label>
            <input type="number" min="1" value={form.quantity} onChange={(e) => setForm({ ...form, quantity: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Expenditure Date</label>
            <input type="date" value={form.expenditureDate} onChange={(e) => setForm({ ...form, expenditureDate: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Reason</label>
            <input type="text" value={form.reason} onChange={(e) => setForm({ ...form, reason: e.target.value })} />
          </div>
          <button type="submit" className="btn btn-primary">Save Expenditure</button>
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
              <th>Reason</th>
              <th>Recorded By</th>
            </tr>
          </thead>
          <tbody>
            {expenditures.map(e => (
              <tr key={e.id}>
                <td>{e.expenditureDate}</td>
                <td>{e.baseName}</td>
                <td>{e.equipmentTypeName}</td>
                <td>{e.quantity}</td>
                <td>{e.reason}</td>
                <td>{e.username}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
