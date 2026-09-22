import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { RollbackEvent } from '../types';
import { RefreshCcw, ShieldAlert, CheckCircle2, Clock } from 'lucide-react';

export const RollbackHistory: React.FC = () => {
  const [rollbacks, setRollbacks] = useState<RollbackEvent[]>([]);

  useEffect(() => {
    devopsService.getRollbackHistory().then(setRollbacks).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
          <RefreshCcw className="w-7 h-7 text-amber-400" />
          <span>Automated Rollback Event History</span>
        </h1>
        <p className="text-slate-400 text-sm">Persisted log of aborted candidate releases, metric breaches, and MTTR recovery durations</p>
      </div>

      <div className="space-y-4">
        {rollbacks.map(rb => (
          <div key={rb.id} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4 shadow-sm">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2">
              <div className="flex items-center space-x-3">
                <div className="p-2.5 bg-red-500/10 text-red-400 rounded-xl">
                  <ShieldAlert className="w-6 h-6" />
                </div>
                <div>
                  <div className="flex items-center space-x-2">
                    <span className="font-bold text-slate-100 text-base">{rb.applicationName}</span>
                    <span className="px-2 py-0.5 rounded text-[10px] font-extrabold uppercase bg-red-500/20 text-red-300 border border-red-500/30">
                      {rb.automatic ? 'AUTOMATIC ROLLBACK' : 'MANUAL ABORT'}
                    </span>
                  </div>
                  <p className="text-xs text-slate-400 font-mono mt-0.5">Stage: {rb.rolloutStage} ({rb.trafficExposure} traffic)</p>
                </div>
              </div>

              <div className="text-right text-xs font-mono text-slate-400">
                <p>MTTR Recovery Time: <strong className="text-emerald-400">{rb.recoveryDurationSeconds}s</strong></p>
                <p>Detected: {new Date(rb.detectedAt).toLocaleString()}</p>
              </div>
            </div>

            <div className="p-4 bg-slate-950 rounded-xl border border-slate-800 space-y-2">
              <div className="flex items-center space-x-2 text-xs font-mono">
                <span className="text-red-400 font-bold">{rb.failedVersion}</span>
                <span className="text-slate-500">→ Restored to →</span>
                <span className="text-emerald-400 font-bold">{rb.restoredVersion}</span>
              </div>
              <p className="text-xs text-slate-300 font-medium">{rb.failureReason}</p>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 text-xs font-mono">
              <div className="p-2.5 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-slate-500 block text-[10px]">Breached Metric</span>
                <span className="font-bold text-slate-200">{rb.failedMetric}</span>
              </div>
              <div className="p-2.5 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-slate-500 block text-[10px]">Measured Value</span>
                <span className="font-bold text-red-400">{rb.measuredValue}</span>
              </div>
              <div className="p-2.5 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-slate-500 block text-[10px]">Configured Threshold</span>
                <span className="font-bold text-slate-200">{rb.thresholdValue}</span>
              </div>
              <div className="p-2.5 bg-slate-950 rounded-lg border border-slate-800">
                <span className="text-slate-500 block text-[10px]">Status</span>
                <span className="font-bold text-emerald-400">{rb.status}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
