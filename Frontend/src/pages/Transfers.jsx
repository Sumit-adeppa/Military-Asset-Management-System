import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Transfers() {
  const { user } = useAuth();
  const [transfers, setTransfers] = useState([]);
  const [bases, setBases] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ sourceBaseId: '', destinationBaseId: '', equipmentTypeId: '', quantity: '', transferDate: '' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data));
      api.get('/equipment-types').then(res => setEquipmentTypes(res.data));
    }
  }, [user]);

  useEffect(() => {
    api.get('/transfers')
      .then(res => setTransfers(res.data))
      .catch(err => console.error('Transfers error:', err))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.sourceBaseId === form.destinationBaseId) {
      alert('Source and Destination bases must be different');
      return;
    }
    try {
      await api.post('/transfers', { ...form, quantity: parseInt(form.quantity) });
      setShowForm(false);
      setForm({ sourceBaseId: '', destinationBaseId: '', equipmentTypeId: '', quantity: '', transferDate: '' });
      const res = await api.get('/transfers');
      setTransfers(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error initiating transfer');
    }
  };

  const handleAction = async (id, action) => {
    try {
      await api.post(`/transfers/${id}/${action}`);
      const res = await api.get('/transfers');
      setTransfers(res.data);
    } catch (err) {
      alert(err.response?.data?.message || `Error ${action} transfer`);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'PENDING': return 'status-pending';
      case 'APPROVED': return 'status-approved';
      case 'COMPLETED': return 'status-completed';
      case 'REJECTED': return 'status-rejected';
      case 'CANCELLED': return 'status-cancelled';
      default: return '';
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Transfers</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'New Transfer'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Source Base</label>
            <select value={form.sourceBaseId} onChange={(e) => setForm({ ...form, sourceBaseId: e.target.value })} required>
              <option value="">Select Source Base</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Destination Base</label>
            <select value={form.destinationBaseId} onChange={(e) => setForm({ ...form, destinationBaseId: e.target.value })} required>
              <option value="">Select Destination Base</option>
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
            <label>Transfer Date</label>
            <input type="datetime-local" value={form.transferDate} onChange={(e) => setForm({ ...form, transferDate: e.target.value })} required />
          </div>
          <button type="submit" className="btn btn-primary">Initiate Transfer</button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>From</th>
              <th>To</th>
              <th>Equipment</th>
              <th>Qty</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {transfers.map(t => (
              <tr key={t.id}>
                <td>{new Date(t.transferDate).toLocaleString()}</td>
                <td>{t.sourceBaseName}</td>
                <td>{t.destinationBaseName}</td>
                <td>{t.equipmentTypeName}</td>
                <td>{t.quantity}</td>
                <td><span className={`status-badge ${getStatusClass(t.status)}`}>{t.status}</span></td>
                <td className="actions">
                  {t.status === 'PENDING' && (
                    <>
                      <button className="btn btn-sm btn-success" onClick={() => handleAction(t.id, 'approve')}>Approve</button>
                      <button className="btn btn-sm btn-danger" onClick={() => handleAction(t.id, 'reject')}>Reject</button>
                    </>
                  )}
                  {t.status === 'APPROVED' && (
                    <button className="btn btn-sm btn-success" onClick={() => handleAction(t.id, 'complete')}>Complete</button>
                  )}
                  {(t.status === 'PENDING' || t.status === 'APPROVED') && (
                    <button className="btn btn-sm btn-warning" onClick={() => handleAction(t.id, 'cancel')}>Cancel</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
