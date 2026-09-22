import React from 'react';
import { GitPullRequest, Activity, Server, Radio, ShieldCheck } from 'lucide-react';

interface NavbarProps {
  argoCdStatus?: string;
  prometheusStatus?: string;
}

export const Navbar: React.FC<NavbarProps> = ({
  argoCdStatus = 'Connected (Synced)',
  prometheusStatus = 'Connected (Scraping)'
}) => {
  return (
    <header className="bg-slate-900 border-b border-slate-800 sticky top-0 z-50">
      <div className="max-w-[1600px] mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="bg-gradient-to-tr from-cyan-500 to-blue-600 p-2 rounded-xl text-white shadow-lg shadow-cyan-500/20">
            <GitPullRequest className="w-6 h-6" />
          </div>
          <div>
            <div className="flex items-center space-x-2">
              <span className="text-xl font-bold bg-gradient-to-r from-white via-slate-200 to-slate-400 bg-clip-text text-transparent">
                GitOps Release Control Center
              </span>
              <span className="bg-cyan-500/10 text-cyan-400 border border-cyan-500/30 text-[10px] font-extrabold uppercase px-2 py-0.5 rounded-full">
                Enterprise v2.0
              </span>
            </div>
            <p className="text-[11px] text-slate-400 font-medium">Metric-Driven Progressive Delivery & Automated Rollback System</p>
          </div>
        </div>

        <div className="flex items-center space-x-4">
          <div className="flex items-center space-x-2 px-3 py-1.5 rounded-lg bg-slate-800/80 border border-slate-700/80 text-xs">
            <Radio className="w-3.5 h-3.5 text-emerald-400 animate-pulse" />
            <span className="text-slate-400">Argo CD:</span>
            <span className="font-semibold text-emerald-400">{argoCdStatus}</span>
          </div>

          <div className="flex items-center space-x-2 px-3 py-1.5 rounded-lg bg-slate-800/80 border border-slate-700/80 text-xs">
            <Activity className="w-3.5 h-3.5 text-blue-400" />
            <span className="text-slate-400">Prometheus:</span>
            <span className="font-semibold text-blue-400">{prometheusStatus}</span>
          </div>

          <div className="flex items-center space-x-2 px-3 py-1.5 rounded-lg bg-slate-800/80 border border-slate-700/80 text-xs">
            <Server className="w-3.5 h-3.5 text-purple-400" />
            <span className="text-slate-400">Cluster:</span>
            <span className="font-semibold text-slate-200">k8s-production</span>
          </div>
        </div>
      </div>
    </header>
  );
};
