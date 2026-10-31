import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Assignments() {
  const { user } = useAuth();
  const [assignments, setAssignments] = useState([]);
  const [personnel, setPersonnel] = useState([]);
  const [bases, setBases] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ personnelId: '', equipmentTypeId: '', baseId: '', quantity: '', assignmentDate: '' });
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data));
      api.get('/equipment-types').then(res => setEquipmentTypes(res.data));
      api.get('/personnel').then(res => setPersonnel(res.data));
    }
  }, [user]);

  const loadAssignments = () => {
    return api.get('/assignments')
      .then(res => setAssignments(res.data))
      .catch(err => console.error('Assignments error:', err));
  };

  useEffect(() => {
    loadAssignments().finally(() => setLoading(false));
  }, []);

  // Refresh when the page regains focus so the table does not show stale statuses
  useEffect(() => {
    const refresh = () => { loadAssignments(); };
    window.addEventListener('focus', refresh);
    return () => window.removeEventListener('focus', refresh);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/assignments', { ...form, quantity: parseInt(form.quantity) });
      setShowForm(false);
      setForm({ personnelId: '', equipmentTypeId: '', baseId: '', quantity: '', assignmentDate: '' });
      const res = await api.get('/assignments');
      setAssignments(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error creating assignment');
    }
  };

  const handleStatusUpdate = async (id, status) => {
    if (updatingId) return; // prevent double-clicks / concurrent updates on the same row
    setUpdatingId(id);
    try {
      await api.put(`/assignments/${id}/status?status=${status}`);
    } catch (err) {
      alert(err.response?.data?.message || 'Error updating status');
    } finally {
      // Always refresh so the UI reflects the true state (fixes stale statuses)
      await loadAssignments();
      setUpdatingId(null);
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'ACTIVE': return 'status-active';
      case 'RETURNED': return 'status-returned';
      case 'LOST': return 'status-lost';
      case 'DAMAGED': return 'status-damaged';
      default: return '';
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Assignments</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'New Assignment'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Personnel</label>
            <select value={form.personnelId} onChange={(e) => setForm({ ...form, personnelId: e.target.value })} required>
              <option value="">Select Personnel</option>
              {personnel.map(p => <option key={p.id} value={p.id}>{p.name} ({p.rank})</option>)}
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
            <label>Assignment Date</label>
            <input type="date" value={form.assignmentDate} onChange={(e) => setForm({ ...form, assignmentDate: e.target.value })} required />
          </div>
          <button type="submit" className="btn btn-primary">Create Assignment</button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Personnel</th>
              <th>Equipment</th>
              <th>Base</th>
              <th>Qty</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {assignments.map(a => (
              <tr key={a.id}>
                <td>{a.assignmentDate}</td>
                <td>{a.personnelName} ({a.personnelRank})</td>
                <td>{a.equipmentTypeName}</td>
                <td>{a.baseName}</td>
                <td>{a.quantity}</td>
                <td><span className={`status-badge ${getStatusClass(a.status)}`}>{a.status}</span></td>
                <td className="actions">
                  {a.status === 'ACTIVE' && (
                    <>
                      <button className="btn btn-sm btn-success" disabled={updatingId === a.id} onClick={() => handleStatusUpdate(a.id, 'RETURNED')}>Return</button>
                      <button className="btn btn-sm btn-danger" disabled={updatingId === a.id} onClick={() => handleStatusUpdate(a.id, 'LOST')}>Lost</button>
                      <button className="btn btn-sm btn-warning" disabled={updatingId === a.id} onClick={() => handleStatusUpdate(a.id, 'DAMAGED')}>Damaged</button>
                    </>
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
