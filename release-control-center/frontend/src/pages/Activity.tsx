import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { ActivityEvent } from '../types';
import { Clock, Activity as ActivityIcon, CheckCircle2, AlertTriangle, ShieldAlert } from 'lucide-react';

export const Activity: React.FC = () => {
  const [events, setEvents] = useState<ActivityEvent[]>([]);

  useEffect(() => {
    devopsService.getActivity().then(setEvents).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
          <Clock className="w-7 h-7 text-cyan-400" />
          <span>Real-time Activity Stream</span>
        </h1>
        <p className="text-slate-400 text-sm">Audit trail of CI builds, GitOps reconciliations, canary analysis, and automated rollbacks</p>
      </div>

      <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
        <div className="space-y-4">
          {events.map(e => (
            <div key={e.id} className="p-4 bg-slate-950 border border-slate-800 rounded-xl flex items-start space-x-4">
              <div className={`p-2 rounded-lg mt-0.5 ${
                e.severity === 'CRITICAL' ? 'bg-red-500/20 text-red-400' :
                e.severity === 'HIGH' ? 'bg-amber-500/20 text-amber-400' :
                'bg-blue-500/20 text-blue-400'
              }`}>
                <ActivityIcon className="w-5 h-5" />
              </div>

              <div className="flex-1 space-y-1">
                <div className="flex justify-between items-center">
                  <span className="font-bold text-slate-100 text-sm">{e.title}</span>
                  <span className="text-xs text-slate-500 font-mono">{new Date(e.timestamp).toLocaleString()}</span>
                </div>
                <p className="text-xs text-slate-300">{e.description}</p>
                {e.applicationName && (
                  <span className="inline-block mt-1 font-mono text-[10px] bg-slate-900 text-slate-400 px-2 py-0.5 rounded border border-slate-800">
                    App: {e.applicationName}
                  </span>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
