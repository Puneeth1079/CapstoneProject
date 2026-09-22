import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { Deployment } from '../types';
import { GitCommit, CheckCircle2, XCircle } from 'lucide-react';

export const Deployments: React.FC = () => {
  const [deployments, setDeployments] = useState<Deployment[]>([]);

  useEffect(() => {
    devopsService.getDeployments().then(setDeployments).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Deployment History</h1>
        <p className="text-slate-400 text-sm">Chronological record of production software delivery releases</p>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
        <table className="w-full text-left text-sm text-slate-300">
          <thead className="bg-slate-800/80 text-xs font-semibold text-slate-400 uppercase tracking-wider">
            <tr>
              <th className="px-6 py-4">Application</th>
              <th className="px-6 py-4">Version</th>
              <th className="px-6 py-4">Commit SHA</th>
              <th className="px-6 py-4">Status</th>
              <th className="px-6 py-4">Author</th>
              <th className="px-6 py-4">Deployed At</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800">
            {deployments.map(dep => (
              <tr key={dep.id} className="hover:bg-slate-800/40 transition">
                <td className="px-6 py-4 font-semibold text-slate-200">{dep.applicationName}</td>
                <td className="px-6 py-4 font-mono text-xs text-cyan-400">{dep.version}</td>
                <td className="px-6 py-4 font-mono text-xs text-slate-400">{dep.commitSha}</td>
                <td className="px-6 py-4">
                  <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${
                    dep.status === 'SUCCESS' ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-red-500/10 text-red-400 border border-red-500/20'
                  }`}>
                    {dep.status}
                  </span>
                </td>
                <td className="px-6 py-4 text-xs text-slate-400">{dep.author}</td>
                <td className="px-6 py-4 text-xs text-slate-500">{new Date(dep.startedAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
