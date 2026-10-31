import { useState, useEffect } from 'react';
import api from '../services/api';

export default function AuditLogs() {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/audit-logs')
      .then(res => setLogs(res.data))
      .catch(err => console.error('AuditLogs error:', err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1>Audit Logs</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Timestamp</th>
              <th>User</th>
              <th>Action</th>
              <th>Entity</th>
              <th>Entity ID</th>
              <th>Details</th>
              <th>Result</th>
            </tr>
          </thead>
          <tbody>
            {logs.map(log => (
              <tr key={log.id}>
                <td>{new Date(log.timestamp).toLocaleString()}</td>
                <td>{log.username || '-'}</td>
                <td>{log.action}</td>
                <td>{log.entity}</td>
                <td>{log.entityId || '-'}</td>
                <td>{log.details}</td>
                <td>{log.resultStatus}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
