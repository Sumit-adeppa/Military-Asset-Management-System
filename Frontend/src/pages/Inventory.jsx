import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Inventory() {
  const { user } = useAuth();
  const [inventory, setInventory] = useState([]);
  const [bases, setBases] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [filters, setFilters] = useState({ baseId: '', equipmentTypeId: '' });
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState(null);
  const [editValue, setEditValue] = useState('');

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/bases').then(res => setBases(res.data));
      api.get('/equipment-types').then(res => setEquipmentTypes(res.data));
    }
  }, [user]);

  const updateFilter = (key, value) => {
    setLoading(true);
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  useEffect(() => {
    const params = {};
    if (filters.baseId) params.baseId = filters.baseId;
    if (filters.equipmentTypeId) params.equipmentTypeId = filters.equipmentTypeId;

    api.get('/inventory', { params })
      .then(res => setInventory(res.data))
      .catch(err => console.error('Inventory error:', err))
      .finally(() => setLoading(false));
  }, [filters]);

  const handleEditOpeningBalance = (item) => {
    setEditingId(item.id);
    setEditValue(item.openingBalance.toString());
  };

  const handleSaveOpeningBalance = async (id) => {
    try {
      await api.put(`/inventory/${id}/opening-balance`, {
        openingBalance: parseInt(editValue),
      });
      setEditingId(null);
      setEditValue('');
      const params = {};
      if (filters.baseId) params.baseId = filters.baseId;
      if (filters.equipmentTypeId) params.equipmentTypeId = filters.equipmentTypeId;
      const res = await api.get('/inventory', { params });
      setInventory(res.data);
    } catch (err) {
      alert(err.response?.data?.message || 'Error updating opening balance');
    }
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditValue('');
  };

  return (
    <div>
      <h1>Inventory</h1>
      <div className="filters">
        {user?.role === 'ADMIN' && (
          <>
            <select value={filters.baseId} onChange={(e) => updateFilter('baseId', e.target.value)}>
              <option value="">All Bases</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
            <select value={filters.equipmentTypeId} onChange={(e) => updateFilter('equipmentTypeId', e.target.value)}>
              <option value="">All Equipment Types</option>
              {equipmentTypes.map(et => <option key={et.id} value={et.id}>{et.name}</option>)}
            </select>
          </>
        )}
      </div>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Base</th>
              <th>Equipment Type</th>
              <th>Quantity</th>
              <th>Opening Balance</th>
              <th>Closing Balance</th>
              {user?.role === 'ADMIN' && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {inventory.map(item => (
              <tr key={item.id}>
                <td>{item.baseName}</td>
                <td>{item.equipmentTypeName}</td>
                <td>{item.quantity}</td>
                <td>
                  {editingId === item.id ? (
                    <div className="edit-cell">
                      <input
                        type="number"
                        value={editValue}
                        onChange={(e) => setEditValue(e.target.value)}
                        min="0"
                      />
                      <button className="btn btn-sm btn-success" onClick={() => handleSaveOpeningBalance(item.id)}>Save</button>
                      <button className="btn btn-sm btn-warning" onClick={handleCancelEdit}>Cancel</button>
                    </div>
                  ) : (
                    item.openingBalance
                  )}
                </td>
                <td>{item.closingBalance}</td>
                {user?.role === 'ADMIN' && (
                  <td className="actions">
                    {editingId !== item.id && (
                      <button className="btn btn-sm btn-warning" onClick={() => handleEditOpeningBalance(item)}>
                        Edit Balance
                      </button>
                    )}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
