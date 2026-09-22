import React from 'react';
import { Settings as SettingsIcon, Server, Database, GitBranch, Radio } from 'lucide-react';

export const Settings: React.FC = () => {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
          <SettingsIcon className="w-7 h-7 text-slate-400" />
          <span>Platform Settings & Integration Configurations</span>
        </h1>
        <p className="text-slate-400 text-sm">DevOps ecosystem endpoints and operational parameters</p>
      </div>

      <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-6 max-w-3xl">
        <div className="space-y-4">
          <h2 className="text-sm font-bold uppercase tracking-wider text-slate-400 flex items-center gap-2">
            <Radio className="w-4 h-4 text-blue-400" />
            <span>DevOps Integrations</span>
          </h2>

          <div className="space-y-3 text-xs">
            <div>
              <label className="block text-slate-400 mb-1">Argo CD API Endpoint</label>
              <input type="text" readOnly value="http://localhost:8080" className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 font-mono text-slate-200" />
            </div>

            <div>
              <label className="block text-slate-400 mb-1">Prometheus Metric Scraper URL</label>
              <input type="text" readOnly value="http://localhost:9090" className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 font-mono text-slate-200" />
            </div>

            <div>
              <label className="block text-slate-400 mb-1">Target Application Endpoint (CloudCart Backend)</label>
              <input type="text" readOnly value="http://localhost:8081" className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 font-mono text-slate-200" />
            </div>

            <div>
              <label className="block text-slate-400 mb-1">GitHub API Repository Path</label>
              <input type="text" readOnly value="capstone-org/capstone-gitops-platform" className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 font-mono text-slate-200" />
            </div>
          </div>
        </div>

        <div className="pt-4 border-t border-slate-800">
          <h2 className="text-sm font-bold uppercase tracking-wider text-slate-400 mb-2">System Version</h2>
          <p className="text-xs text-slate-300 font-mono">GitOps Release Control Center v2.0 (Spring Boot 3.2 + React 18)</p>
        </div>
      </div>
    </div>
  );
};
