import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { Rollout } from '../types';
import { Layers, Play, Pause, AlertOctagon, RotateCcw, CheckCircle2, XCircle, ArrowRight } from 'lucide-react';

export const ProgressiveDelivery: React.FC = () => {
  const [rollout, setRollout] = useState<Rollout | null>(null);
  const [loading, setLoading] = useState(true);
  const [actionMessage, setActionMessage] = useState<string | null>(null);

  const fetchRollout = () => {
    devopsService.getRollout('cloudcart-backend')
      .then(data => {
        setRollout(data);
        setLoading(false);
      })
      .catch(console.error);
  };

  useEffect(() => {
    fetchRollout();
    const interval = setInterval(fetchRollout, 3000);
    return () => clearInterval(interval);
  }, []);

  const handleAction = async (action: 'promote' | 'pause' | 'resume' | 'abort' | 'retry') => {
    if (!rollout) return;
    setActionMessage(`Executing ${action.toUpperCase()} operation on Argo Rollout...`);
    try {
      let updated: Rollout;
      if (action === 'promote') updated = await devopsService.promoteRollout(rollout.applicationName);
      else if (action === 'pause') updated = await devopsService.pauseRollout(rollout.applicationName);
      else if (action === 'resume') updated = await devopsService.resumeRollout(rollout.applicationName);
      else if (action === 'abort') updated = await devopsService.abortRollout(rollout.applicationName);
      else updated = await devopsService.retryRollout(rollout.applicationName);

      setRollout(updated);
      setActionMessage(`Operation ${action.toUpperCase()} completed successfully.`);
    } catch (err: any) {
      setActionMessage(`Operation failed: ${err.message}`);
    }
  };

  if (loading || !rollout) {
    return <div className="p-8 text-slate-400">Loading Argo Rollout Canary State...</div>;
  }

  const steps = [10, 25, 50, 75, 100];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
            <Layers className="w-7 h-7 text-blue-400" />
            <span>Progressive Delivery Control Center</span>
          </h1>
          <p className="text-slate-400 text-sm">Argo Rollouts Canary progression management and live operator actions</p>
        </div>

        {/* Live Operator Action Controls */}
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => handleAction('promote')}
            className="flex items-center space-x-1.5 bg-blue-600 hover:bg-blue-500 text-white text-xs font-semibold px-3 py-2 rounded-xl transition shadow-lg shadow-blue-600/20"
          >
            <Play className="w-3.5 h-3.5 fill-white" />
            <span>Promote</span>
          </button>

          <button
            onClick={() => handleAction('pause')}
            className="flex items-center space-x-1.5 bg-amber-600 hover:bg-amber-500 text-white text-xs font-semibold px-3 py-2 rounded-xl transition"
          >
            <Pause className="w-3.5 h-3.5" />
            <span>Pause</span>
          </button>

          <button
            onClick={() => handleAction('resume')}
            className="flex items-center space-x-1.5 bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-semibold px-3 py-2 rounded-xl transition"
          >
            <Play className="w-3.5 h-3.5" />
            <span>Resume</span>
          </button>

          <button
            onClick={() => handleAction('abort')}
            className="flex items-center space-x-1.5 bg-red-600 hover:bg-red-500 text-white text-xs font-semibold px-3 py-2 rounded-xl transition shadow-lg shadow-red-600/20"
          >
            <AlertOctagon className="w-3.5 h-3.5" />
            <span>Abort / Rollback</span>
          </button>

          <button
            onClick={() => handleAction('retry')}
            className="flex items-center space-x-1.5 bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-semibold px-3 py-2 rounded-xl border border-slate-700 transition"
          >
            <RotateCcw className="w-3.5 h-3.5" />
            <span>Retry</span>
          </button>
        </div>
      </div>

      {actionMessage && (
        <div className="p-4 bg-slate-900 border border-blue-800 text-blue-300 text-xs font-medium rounded-xl">
          {actionMessage}
        </div>
      )}

      {/* Main Rollout Card */}
      <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-6">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 p-4 bg-slate-950 rounded-xl border border-slate-800 text-xs">
          <div>
            <span className="text-slate-500">Application</span>
            <p className="font-bold text-slate-100 text-sm mt-0.5">{rollout.applicationName}</p>
          </div>
          <div>
            <span className="text-slate-500">Namespace</span>
            <p className="font-mono text-slate-300 mt-0.5">{rollout.namespace}</p>
          </div>
          <div>
            <span className="text-slate-500">Stable Version</span>
            <p className="font-mono font-bold text-emerald-400 mt-0.5">{rollout.stableVersion}</p>
          </div>
          <div>
            <span className="text-slate-500">Canary Version</span>
            <p className="font-mono font-bold text-cyan-400 mt-0.5">{rollout.canaryVersion || 'N/A'}</p>
          </div>
        </div>

        {/* Stepper Stage Bar */}
        <div className="space-y-3">
          <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">Canary Traffic Weight Progression</h3>
          <div className="grid grid-cols-5 gap-3">
            {steps.map((weight, idx) => {
              const isCurrent = rollout.currentWeight === weight;
              const isPassed = rollout.currentWeight > weight;
              return (
                <div
                  key={weight}
                  className={`p-4 rounded-xl border text-center transition ${
                    isCurrent
                      ? 'bg-blue-600/20 border-blue-500 text-blue-300 ring-2 ring-blue-500/50'
                      : isPassed
                      ? 'bg-slate-950 border-emerald-800 text-emerald-400'
                      : 'bg-slate-950 border-slate-800 text-slate-500'
                  }`}
                >
                  <p className="text-xs font-semibold">Step {idx + 1}</p>
                  <p className="text-lg font-extrabold my-1">{weight}%</p>
                  <span className="text-[10px] uppercase font-bold">
                    {isCurrent ? 'ACTIVE CANARY' : isPassed ? 'PASSED' : 'PENDING'}
                  </span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Live Prometheus Metrics Analysis */}
        <div className="space-y-3 pt-2">
          <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">AnalysisTemplate Evaluation Status</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {rollout.metricsAnalysis?.map((m: any, idx: number) => (
              <div key={idx} className="p-4 bg-slate-950 border border-slate-800 rounded-xl flex items-center justify-between">
                <div>
                  <p className="text-xs font-semibold text-slate-300">{m.name}</p>
                  <p className="text-base font-bold text-white mt-1">{m.value}</p>
                  <p className="text-[11px] text-slate-500">Threshold: {m.threshold}</p>
                </div>
                {m.status === 'PASS' ? (
                  <CheckCircle2 className="w-6 h-6 text-emerald-400" />
                ) : (
                  <XCircle className="w-6 h-6 text-red-400" />
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
