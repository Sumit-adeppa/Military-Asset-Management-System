import { useState, useEffect } from 'react';
import api from '../services/api';

export default function EquipmentTypes() {
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ name: '', category: '', description: '', serializationType: 'QUANTITY' });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/equipment-types')
      .then(res => setEquipmentTypes(res.data))
      .catch(err => console.error('EquipmentTypes error:', err))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({ name: '', category: '', description: '', serializationType: 'QUANTITY' });
    setEditingId(null);
    setShowForm(false);
  };

  const handleEdit = (et) => {
    setForm({
      name: et.name,
      category: et.category || '',
      description: et.description || '',
      serializationType: et.serializationType,
    });
    setEditingId(et.id);
    setShowForm(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.put(`/equipment-types/${editingId}`, form);
      } else {
        await api.post('/equipment-types', form);
      }
      resetForm();
      const res = await api.get('/equipment-types');
      setEquipmentTypes(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error saving equipment type');
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this equipment type?')) return;
    try {
      await api.delete(`/equipment-types/${id}`);
      setEquipmentTypes(equipmentTypes.filter(et => et.id !== id));
    } catch (err) {
      alert(err.response?.data?.message || 'Error deleting equipment type');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Equipment Types</h1>
        <button className="btn btn-primary" onClick={() => {
          if (showForm && editingId) {
            resetForm();
          } else {
            setShowForm(!showForm);
          }
        }}>
          {showForm ? 'Cancel' : 'Add Equipment Type'}
        </button>
      </div>
      {showForm && (
        <form className="data-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name</label>
            <input type="text" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          </div>
          <div className="form-group">
            <label>Category</label>
            <input type="text" value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} />
          </div>
          <div className="form-group">
            <label>Description</label>
            <input type="text" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </div>
          <div className="form-group">
            <label>Serialization Type</label>
            <select value={form.serializationType} onChange={(e) => setForm({ ...form, serializationType: e.target.value })} required>
              <option value="QUANTITY">Quantity-Based</option>
              <option value="SERIALIZED">Serialized</option>
            </select>
          </div>
          <button type="submit" className="btn btn-primary">
            {editingId ? 'Update Equipment Type' : 'Save Equipment Type'}
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
              <th>Category</th>
              <th>Description</th>
              <th>Type</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {equipmentTypes.map(et => (
              <tr key={et.id}>
                <td>{et.name}</td>
                <td>{et.category}</td>
                <td>{et.description}</td>
                <td>{et.serializationType}</td>
                <td className="actions">
                  <button className="btn btn-sm btn-warning" onClick={() => handleEdit(et)}>Edit</button>
                  <button className="btn btn-sm btn-danger" onClick={() => handleDelete(et.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
