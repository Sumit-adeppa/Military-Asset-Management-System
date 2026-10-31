import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthProvider';
import { useAuth } from './contexts/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Inventory from './pages/Inventory';
import Purchases from './pages/Purchases';
import Transfers from './pages/Transfers';
import Assignments from './pages/Assignments';
import Expenditures from './pages/Expenditures';
import Personnel from './pages/Personnel';
import Users from './pages/Users';
import Bases from './pages/Bases';
import EquipmentTypes from './pages/EquipmentTypes';
import AuditLogs from './pages/AuditLogs';

function ProtectedRoute({ children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  return <Layout>{children}</Layout>;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/inventory" element={<ProtectedRoute><Inventory /></ProtectedRoute>} />
          <Route path="/purchases" element={<ProtectedRoute><Purchases /></ProtectedRoute>} />
          <Route path="/transfers" element={<ProtectedRoute><Transfers /></ProtectedRoute>} />
          <Route path="/assignments" element={<ProtectedRoute><Assignments /></ProtectedRoute>} />
          <Route path="/expenditures" element={<ProtectedRoute><Expenditures /></ProtectedRoute>} />
          <Route path="/personnel" element={<ProtectedRoute><Personnel /></ProtectedRoute>} />
          <Route path="/users" element={<ProtectedRoute><Users /></ProtectedRoute>} />
          <Route path="/bases" element={<ProtectedRoute><Bases /></ProtectedRoute>} />
          <Route path="/equipment-types" element={<ProtectedRoute><EquipmentTypes /></ProtectedRoute>} />
          <Route path="/audit-logs" element={<ProtectedRoute><AuditLogs /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
