import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { devopsService } from '../services/devopsService';
import { Application } from '../types';
import { Server, ShieldCheck, Box, Activity } from 'lucide-react';

export const ApplicationDetails: React.FC = () => {
  const { name } = useParams<{ name: string }>();
  const [app, setApp] = useState<Application | null>(null);

  useEffect(() => {
    if (name) {
      devopsService.getApplicationByName(name).then(setApp).catch(console.error);
    }
  }, [name]);

  if (!app) {
    return <div className="p-8 text-slate-400">Loading Application Details for {name}...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">{app.name} Details</h1>
          <p className="text-slate-400 text-sm">Kubernetes deployment topology and container pod status</p>
        </div>
        <span className="px-3 py-1 rounded-full text-xs font-bold bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
          {app.healthStatus}
        </span>
      </div>

      <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
        <h2 className="text-lg font-bold text-slate-200 flex items-center gap-2">
          <Box className="w-5 h-5 text-blue-400" />
          <span>Running Pod Replicas</span>
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {app.pods?.map((pod, idx) => (
            <div key={idx} className="p-4 bg-slate-950 border border-slate-800 rounded-xl space-y-2">
              <div className="flex justify-between items-start">
                <span className="font-mono text-xs font-semibold text-slate-200 truncate">{pod.name}</span>
                <span className={`text-[10px] font-bold px-2 py-0.5 rounded ${pod.type === 'canary' ? 'bg-cyan-500/20 text-cyan-300' : 'bg-emerald-500/20 text-emerald-300'}`}>
                  {pod.type.toUpperCase()}
                </span>
              </div>
              <div className="flex justify-between text-xs text-slate-400 font-mono">
                <span>Version: {pod.version}</span>
                <span>Restarts: {pod.restarts}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
