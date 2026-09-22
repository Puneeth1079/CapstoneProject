import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Navbar } from './components/Navbar';
import { Sidebar } from './components/Sidebar';
import { Dashboard } from './pages/Dashboard';
import { Products } from './pages/Products';
import { Orders } from './pages/Orders';
import { Inventory } from './pages/Inventory';
import { Customers } from './pages/Customers';
import { ExperimentControl } from './pages/ExperimentControl';
import { faultService } from './services/faultService';

export const App: React.FC = () => {
  const [activeFaultMode, setActiveFaultMode] = useState('NORMAL');

  useEffect(() => {
    const checkFault = () => {
      faultService.getFaultMode()
        .then(res => setActiveFaultMode(res.activeMode))
        .catch(() => {});
    };
    checkFault();
    const interval = setInterval(checkFault, 4000);
    return () => clearInterval(interval);
  }, []);

  return (
    <Router>
      <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col">
        <Navbar activeFaultMode={activeFaultMode} />
        <div className="flex flex-1">
          <Sidebar />
          <main className="flex-1 p-8 max-w-7xl">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/products" element={<Products />} />
              <Route path="/orders" element={<Orders />} />
              <Route path="/inventory" element={<Inventory />} />
              <Route path="/customers" element={<Customers />} />
              <Route path="/experiment" element={<ExperimentControl />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
};

export default App;
