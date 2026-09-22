import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard, Server, GitCommit, Layers, LineChart,
  GitBranch, RefreshCcw, Gauge, Clock, Flame, Settings, Activity
} from 'lucide-react';

export const Sidebar: React.FC = () => {
  const menuItems = [
    { to: '/', label: 'Overview', icon: LayoutDashboard },
    { to: '/applications', label: 'Applications', icon: Server },
    { to: '/deployments', label: 'Deployments', icon: GitCommit },
    { to: '/progressive-delivery', label: 'Progressive Delivery', icon: Layers },
    { to: '/metrics', label: 'Metrics', icon: LineChart },
    { to: '/pipelines', label: 'Pipelines', icon: GitBranch },
    { to: '/gitops', label: 'GitOps', icon: Activity },
    { to: '/rollback-history', label: 'Rollback History', icon: RefreshCcw },
    { to: '/dora-metrics', label: 'DORA Metrics', icon: Gauge },
    { to: '/activity', label: 'Activity Feed', icon: Clock },
    { to: '/experiments', label: 'Release Experiments', icon: Flame },
    { to: '/settings', label: 'Settings', icon: Settings },
  ];

  return (
    <aside className="w-64 bg-slate-900/90 border-r border-slate-800 min-h-[calc(100vh-4rem)] p-4 flex flex-col justify-between shrink-0">
      <div className="space-y-6">
        <div>
          <p className="px-4 text-[10px] font-extrabold uppercase tracking-widest text-slate-500 mb-3">
            Core Release Operations
          </p>
          <nav className="space-y-1">
            {menuItems.slice(0, 7).map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.to}
                  to={item.to}
                  className={({ isActive }) =>
                    `flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-xs font-medium transition ${
                      isActive
                        ? 'bg-blue-600 text-white shadow-lg shadow-blue-600/25'
                        : 'text-slate-400 hover:bg-slate-800/80 hover:text-slate-200'
                    }`
                  }
                >
                  <Icon className="w-4 h-4 shrink-0" />
                  <span>{item.label}</span>
                </NavLink>
              );
            })}
          </nav>
        </div>

        <div>
          <p className="px-4 text-[10px] font-extrabold uppercase tracking-widest text-slate-500 mb-3">
            Observability & Governance
          </p>
          <nav className="space-y-1">
            {menuItems.slice(7).map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.to}
                  to={item.to}
                  className={({ isActive }) =>
                    `flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-xs font-medium transition ${
                      isActive
                        ? 'bg-blue-600 text-white shadow-lg shadow-blue-600/25'
                        : 'text-slate-400 hover:bg-slate-800/80 hover:text-slate-200'
                    }`
                  }
                >
                  <Icon className="w-4 h-4 shrink-0" />
                  <span>{item.label}</span>
                </NavLink>
              );
            })}
          </nav>
        </div>
      </div>

      <div className="p-3.5 bg-slate-950 rounded-xl border border-slate-800 text-[11px] text-slate-400 space-y-1">
        <div className="flex items-center justify-between font-semibold text-slate-200">
          <span>Argo Rollouts Canary</span>
          <span className="text-emerald-400 text-[10px]">ACTIVE</span>
        </div>
        <p className="text-slate-500">Prometheus AnalysisTemplate continuously monitoring HTTP & Latency thresholds.</p>
      </div>
    </aside>
  );
};
