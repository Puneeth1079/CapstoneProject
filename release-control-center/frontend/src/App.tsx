import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Navbar } from './components/Navbar';
import { Sidebar } from './components/Sidebar';
import { Overview } from './pages/Overview';
import { Applications } from './pages/Applications';
import { ApplicationDetails } from './pages/ApplicationDetails';
import { Deployments } from './pages/Deployments';
import { ProgressiveDelivery } from './pages/ProgressiveDelivery';
import { Metrics } from './pages/Metrics';
import { Pipelines } from './pages/Pipelines';
import { GitOps } from './pages/GitOps';
import { RollbackHistory } from './pages/RollbackHistory';
import { DoraMetricsPage } from './pages/DoraMetricsPage';
import { Activity } from './pages/Activity';
import { ReleaseExperiments } from './pages/ReleaseExperiments';
import { Settings } from './pages/Settings';

export const App: React.FC = () => {
  return (
    <Router>
      <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans">
        <Navbar />
        <div className="flex flex-1">
          <Sidebar />
          <main className="flex-1 p-6 md:p-8 max-w-[1600px]">
            <Routes>
              <Route path="/" element={<Overview />} />
              <Route path="/applications" element={<Applications />} />
              <Route path="/applications/:name" element={<ApplicationDetails />} />
              <Route path="/deployments" element={<Deployments />} />
              <Route path="/progressive-delivery" element={<ProgressiveDelivery />} />
              <Route path="/metrics" element={<Metrics />} />
              <Route path="/pipelines" element={<Pipelines />} />
              <Route path="/gitops" element={<GitOps />} />
              <Route path="/rollback-history" element={<RollbackHistory />} />
              <Route path="/dora-metrics" element={<DoraMetricsPage />} />
              <Route path="/activity" element={<Activity />} />
              <Route path="/experiments" element={<ReleaseExperiments />} />
              <Route path="/settings" element={<Settings />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
};

export default App;
