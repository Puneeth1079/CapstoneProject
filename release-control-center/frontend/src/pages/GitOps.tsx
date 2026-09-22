import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { GitOpsStatus } from '../types';
import { Activity, GitPullRequest, ArrowRight, ShieldCheck, CheckCircle2 } from 'lucide-react';

export const GitOps: React.FC = () => {
  const [apps, setApps] = useState<GitOpsStatus[]>([]);

  useEffect(() => {
    devopsService.getGitOpsApps().then(setApps).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
          <Activity className="w-7 h-7 text-cyan-400" />
          <span>Argo CD GitOps Synchronization</span>
        </h1>
        <p className="text-slate-400 text-sm">Desired vs Live Kubernetes Cluster State Reconciliation</p>
      </div>

      {/* GitOps Flow Diagram */}
      <div className="p-6 bg-slate-900 border border-slate-800 rounded-2xl space-y-4">
        <h2 className="text-sm font-bold uppercase tracking-wider text-slate-400">GitOps Architecture Flow</h2>
        <div className="flex flex-col md:flex-row items-center justify-around gap-4 p-4 bg-slate-950 rounded-xl border border-slate-800">
          <div className="text-center p-3 bg-slate-900 rounded-xl border border-slate-800 w-full md:w-56">
            <GitPullRequest className="w-6 h-6 text-purple-400 mx-auto mb-1" />
            <span className="font-bold text-slate-200 text-sm">Git Repository</span>
            <p className="text-[11px] text-slate-500 font-mono">manifests/production</p>
          </div>
          <ArrowRight className="w-6 h-6 text-slate-600 hidden md:block" />
          <div className="text-center p-3 bg-slate-900 rounded-xl border border-slate-800 w-full md:w-56">
            <Activity className="w-6 h-6 text-cyan-400 mx-auto mb-1 animate-pulse" />
            <span className="font-bold text-slate-200 text-sm">Argo CD Controller</span>
            <p className="text-[11px] text-slate-500 font-mono">Auto-Sync / Self-Heal</p>
          </div>
          <ArrowRight className="w-6 h-6 text-slate-600 hidden md:block" />
          <div className="text-center p-3 bg-slate-900 rounded-xl border border-slate-800 w-full md:w-56">
            <ShieldCheck className="w-6 h-6 text-emerald-400 mx-auto mb-1" />
            <span className="font-bold text-slate-200 text-sm">Kubernetes Cluster</span>
            <p className="text-[11px] text-slate-500 font-mono">Argo Rollouts Canary</p>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {apps.map((app, idx) => (
          <div key={idx} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <div className="flex justify-between items-center">
              <h3 className="font-bold text-lg text-slate-100">{app.applicationName}</h3>
              <div className="flex space-x-2">
                <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  {app.syncStatus}
                </span>
                <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-blue-500/10 text-blue-400 border border-blue-500/20">
                  {app.healthStatus}
                </span>
              </div>
            </div>

            <div className="p-4 bg-slate-950 rounded-xl border border-slate-800 text-xs font-mono space-y-1.5 text-slate-300">
              <p><span className="text-slate-500">Git Revision:</span> {app.gitRevision}</p>
              <p><span className="text-slate-500">Repository:</span> {app.repositoryUrl}</p>
              <p><span className="text-slate-500">Path:</span> {app.repositoryPath}</p>
              <p><span className="text-slate-500">Target Cluster:</span> {app.cluster}</p>
            </div>

            <div className="grid grid-cols-3 gap-2 text-center text-xs">
              <div className="p-2 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-[10px] text-slate-500 block">Auto Sync</span>
                <span className="font-bold text-emerald-400">ENABLED</span>
              </div>
              <div className="p-2 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-[10px] text-slate-500 block">Self Heal</span>
                <span className="font-bold text-emerald-400">ENABLED</span>
              </div>
              <div className="p-2 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-[10px] text-slate-500 block">Auto Prune</span>
                <span className="font-bold text-emerald-400">ENABLED</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
