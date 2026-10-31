import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Layout({ children }) {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const navItems = [
    { path: '/', label: 'Dashboard', icon: '📊', roles: ['ADMIN', 'BASE_COMMANDER', 'LOGISTICS_OFFICER'] },
    { path: '/inventory', label: 'Inventory', icon: '📦', roles: ['ADMIN', 'BASE_COMMANDER', 'LOGISTICS_OFFICER'] },
    { path: '/purchases', label: 'Purchases', icon: '🛒', roles: ['ADMIN', 'BASE_COMMANDER', 'LOGISTICS_OFFICER'] },
    { path: '/transfers', label: 'Transfers', icon: '🔄', roles: ['ADMIN', 'BASE_COMMANDER', 'LOGISTICS_OFFICER'] },
    { path: '/assignments', label: 'Assignments', icon: '👤', roles: ['ADMIN', 'BASE_COMMANDER'] },
    { path: '/expenditures', label: 'Expenditures', icon: '💸', roles: ['ADMIN', 'BASE_COMMANDER'] },
    { path: '/personnel', label: 'Personnel', icon: '👥', roles: ['ADMIN', 'BASE_COMMANDER'] },
    { path: '/users', label: 'Users', icon: '🔑', roles: ['ADMIN'] },
    { path: '/bases', label: 'Bases', icon: '🏢', roles: ['ADMIN'] },
    { path: '/equipment-types', label: 'Equipment Types', icon: '🔧', roles: ['ADMIN'] },
    { path: '/audit-logs', label: 'Audit Logs', icon: '📋', roles: ['ADMIN'] },
  ];

  const visibleNavItems = navItems.filter(item =>
    user && item.roles.includes(user.role)
  );

  return (
    <div className="app-container">
      <nav className={`sidebar ${sidebarOpen ? 'open' : 'closed'}`}>
        <div className="sidebar-header">
          <h2>MAMS</h2>
          <button className="toggle-btn" onClick={() => setSidebarOpen(!sidebarOpen)}>
            {sidebarOpen ? '◀' : '▶'}
          </button>
        </div>
        <ul className="nav-list">
          {visibleNavItems.map(item => (
            <li key={item.path}>
              <Link
                to={item.path}
                className={location.pathname === item.path ? 'nav-link active' : 'nav-link'}
              >
                <span className="nav-icon">{item.icon}</span>
                {sidebarOpen && <span className="nav-label">{item.label}</span>}
              </Link>
            </li>
          ))}
        </ul>
      </nav>
      <div className="main-content">
        <header className="top-bar">
          <div className="user-info">
            <span className="user-name">{user?.username}</span>
            <span className="user-role">{user?.role}</span>
            {user?.baseName && <span className="user-base">{user.baseName}</span>}
          </div>
          <button className="logout-btn" onClick={handleLogout}>Logout</button>
        </header>
        <main className="page-content">{children}</main>
      </div>
    </div>
  );
}
