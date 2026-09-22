import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { Application } from '../types';
import { Server, CheckCircle2, GitBranch, RefreshCw } from 'lucide-react';
import { Link } from 'react-router-dom';

export const Applications: React.FC = () => {
  const [apps, setApps] = useState<Application[]>([]);

  useEffect(() => {
    devopsService.getApplications().then(setApps).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Managed Kubernetes Applications</h1>
        <p className="text-slate-400 text-sm">Applications synchronized via Argo CD and delivered via Argo Rollouts</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {apps.map(app => (
          <div key={app.id} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <div className="flex justify-between items-start">
              <div className="flex items-center space-x-3">
                <div className="p-3 bg-blue-500/10 text-blue-400 rounded-xl">
                  <Server className="w-6 h-6" />
                </div>
                <div>
                  <h3 className="font-bold text-lg text-slate-100">{app.name}</h3>
                  <p className="text-xs text-slate-400 font-mono">Namespace: {app.namespace}</p>
                </div>
              </div>

              <div className="flex space-x-2">
                <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  {app.syncStatus}
                </span>
                <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-blue-500/10 text-blue-400 border border-blue-500/20">
                  {app.healthStatus}
                </span>
              </div>
            </div>

            <div className="p-3 bg-slate-950 rounded-xl border border-slate-800 text-xs text-slate-300 font-mono space-y-1">
              <p><span className="text-slate-500">Repository:</span> {app.gitRepo}</p>
              <p><span className="text-slate-500">Target Revision:</span> {app.targetRevision}</p>
            </div>

            <div className="pt-2 flex justify-between items-center text-xs">
              <div className="flex space-x-4 text-slate-400">
                <span>Auto-Sync: <strong className="text-emerald-400">ENABLED</strong></span>
                <span>Self-Heal: <strong className="text-emerald-400">ENABLED</strong></span>
              </div>
              <Link to={`/applications/${app.name}`} className="font-semibold text-blue-400 hover:text-blue-300">
                View Topology →
              </Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
