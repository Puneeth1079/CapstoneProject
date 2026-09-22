import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { Experiment } from '../types';
import { Flame, RefreshCw, CheckCircle, Clock, ShieldAlert, AlertTriangle, Cpu, HardDrive } from 'lucide-react';

export const ReleaseExperiments: React.FC = () => {
  const [activeExp, setActiveExp] = useState<Experiment | null>(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    fetchActive();
  }, []);

  const fetchActive = () => {
    devopsService.getActiveExperiment().then(setActiveExp).catch(console.error);
  };

  const handleTrigger = async (mode: string) => {
    setLoading(true);
    setMessage(null);
    try {
      const exp = await devopsService.triggerExperiment(mode, `Controlled injection: ${mode}`);
      setActiveExp(exp);
      setMessage(`Successfully triggered ${mode} experiment! Prometheus metrics will reflect degradation.`);
    } catch (err: any) {
      setMessage(`Experiment trigger failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleReset = async () => {
    setLoading(true);
    setMessage(null);
    try {
      const exp = await devopsService.resetExperiment();
      setActiveExp(exp);
      setMessage('Release Experiment reset to NORMAL. Baseline operation restored.');
    } catch (err: any) {
      setMessage(`Reset failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const experimentCards = [
    { mode: 'NORMAL', title: 'Baseline Normal', desc: 'Optimal performance. Success Rate >= 99%, P95 <= 150ms.', icon: CheckCircle, color: 'border-emerald-700 bg-emerald-950/20 text-emerald-400' },
    { mode: 'HIGH_LATENCY', title: 'Latency Degraded Release', desc: 'Injects bounded 2.5s delay to trigger P95 latency analysis failure.', icon: Clock, color: 'border-amber-700 bg-amber-950/20 text-amber-400' },
    { mode: 'HTTP_ERRORS', title: 'HTTP 500 Failure Release', desc: 'Injects 50% HTTP errors to trigger HTTP Success Rate analysis breach.', icon: ShieldAlert, color: 'border-red-700 bg-red-950/20 text-red-400' },
    { mode: 'FAILED_TRANSACTIONS', title: 'Transaction Failure Mode', desc: 'Forces order creation transaction failures.', icon: AlertTriangle, color: 'border-orange-700 bg-orange-950/20 text-orange-400' },
    { mode: 'CPU_PRESSURE', title: 'High CPU Stress Mode', desc: 'Executes CPU calculation loops on target pods.', icon: Cpu, color: 'border-purple-700 bg-purple-950/20 text-purple-400' },
    { mode: 'MEMORY_PRESSURE', title: 'High Memory Stress Mode', desc: 'Simulates memory allocation pressure on target pods.', icon: HardDrive, color: 'border-blue-700 bg-blue-950/20 text-blue-400' },
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
            <Flame className="w-7 h-7 text-amber-500" />
            <span>Progressive Delivery Release Experiments</span>
          </h1>
          <p className="text-slate-400 text-sm">Inject failure modes to demonstrate Prometheus metric evaluation & automated Argo Rollout rollback</p>
        </div>

        <button
          onClick={handleReset}
          disabled={loading}
          className="flex items-center space-x-2 bg-slate-800 hover:bg-slate-700 text-slate-200 font-semibold px-4 py-2 rounded-xl border border-slate-700 transition text-xs"
        >
          <RefreshCw className="w-4 h-4" />
          <span>Reset Experiment State</span>
        </button>
      </div>

      {activeExp && (
        <div className="p-6 bg-slate-900 border border-slate-800 rounded-2xl flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
          <div>
            <span className="text-xs font-semibold uppercase text-slate-400">Current Experiment Mode</span>
            <div className="flex items-center space-x-3 mt-1">
              <span className={`text-xl font-bold ${activeExp.active ? 'text-amber-400' : 'text-emerald-400'}`}>
                {activeExp.mode}
              </span>
              <span className={`text-xs font-bold px-2.5 py-0.5 rounded-full ${
                activeExp.active ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
              }`}>
                {activeExp.status}
              </span>
            </div>
            <p className="text-xs text-slate-400 mt-1">{activeExp.description}</p>
          </div>
        </div>
      )}

      {message && (
        <div className="p-4 bg-blue-950/40 border border-blue-800 rounded-xl text-xs font-medium text-blue-300">
          {message}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {experimentCards.map(c => {
          const Icon = c.icon;
          const isActive = activeExp?.mode === c.mode;
          return (
            <div
              key={c.mode}
              className={`p-6 rounded-2xl border transition flex flex-col justify-between space-y-4 ${c.color} ${
                isActive ? 'ring-2 ring-blue-500 shadow-xl' : 'hover:border-slate-700 bg-slate-900'
              }`}
            >
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Icon className="w-6 h-6" />
                  {isActive && <span className="text-xs font-bold uppercase bg-blue-600 text-white px-2 py-0.5 rounded">Active</span>}
                </div>
                <h3 className="font-bold text-slate-100 text-base">{c.title}</h3>
                <p className="text-xs text-slate-400 leading-relaxed">{c.desc}</p>
              </div>

              <button
                onClick={() => handleTrigger(c.mode)}
                disabled={loading || isActive}
                className={`w-full py-2.5 rounded-xl font-semibold text-xs uppercase tracking-wider transition ${
                  isActive
                    ? 'bg-slate-800 text-slate-500 cursor-not-allowed'
                    : 'bg-blue-600 hover:bg-blue-500 text-white shadow-lg shadow-blue-600/20'
                }`}
              >
                {isActive ? 'Active Mode' : `Trigger ${c.mode}`}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};
