import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Dashboard() {
  const { user } = useAuth();
  const [data, setData] = useState(null);
  const [bases, setBases] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [filters, setFilters] = useState({
    baseId: '',
    equipmentTypeId: '',
    startDate: '',
    endDate: '',
  });
  const [loading, setLoading] = useState(true);

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
    if (filters.startDate) params.startDate = filters.startDate;
    if (filters.endDate) params.endDate = filters.endDate;

    api.get('/dashboard', { params })
      .then(res => setData(res.data))
      .catch(err => console.error('Dashboard error:', err))
      .finally(() => setLoading(false));
  }, [filters]);

  const cards = [
    { label: 'Opening Balance', value: data?.openingBalance, color: 'blue' },
    { label: 'Purchases', value: data?.purchases, color: 'green' },
    { label: 'Transfer In', value: data?.transferIn, color: 'teal' },
    { label: 'Transfer Out', value: data?.transferOut, color: 'orange' },
    { label: 'Net Movement', value: data?.netMovement, color: 'purple' },
    { label: 'Assigned Assets', value: data?.assigned, color: 'indigo' },
    { label: 'Expended Assets', value: data?.expended, color: 'red' },
    { label: 'Closing Balance', value: data?.closingBalance, color: 'dark' },
  ];

  return (
    <div>
      <h1>Dashboard</h1>
      <div className="filters">
        {user?.role === 'ADMIN' && (
          <>
            <select
              value={filters.baseId}
              onChange={(e) => updateFilter('baseId', e.target.value)}
            >
              <option value="">All Bases</option>
              {bases.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
            </select>
            <select
              value={filters.equipmentTypeId}
              onChange={(e) => updateFilter('equipmentTypeId', e.target.value)}
            >
              <option value="">All Equipment Types</option>
              {equipmentTypes.map(et => <option key={et.id} value={et.id}>{et.name}</option>)}
            </select>
          </>
        )}
        <input
          type="date"
          value={filters.startDate}
          onChange={(e) => updateFilter('startDate', e.target.value)}
        />
        <input
          type="date"
          value={filters.endDate}
          onChange={(e) => updateFilter('endDate', e.target.value)}
        />
      </div>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="dashboard-grid">
          {cards.map(card => (
            <div key={card.label} className={`card card-${card.color}`}>
              <div className="card-label">{card.label}</div>
              <div className="card-value">{card.value ?? 0}</div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
