import React, { useEffect, useState } from 'react';
import { faultService } from '../services/faultService';
import { FaultModeState } from '../types';
import { Flame, RefreshCw, ShieldAlert, CheckCircle, Clock, AlertTriangle, Cpu, HardDrive } from 'lucide-react';

export const ExperimentControl: React.FC = () => {
  const [faultState, setFaultState] = useState<FaultModeState | null>(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    fetchFaultState();
  }, []);

  const fetchFaultState = () => {
    faultService.getFaultMode().then(setFaultState).catch(console.error);
  };

  const handleSetMode = async (mode: string) => {
    setLoading(true);
    setMessage(null);
    try {
      const updated = await faultService.setFaultMode(mode);
      setFaultState(updated);
      setMessage(`Activated failure mode: ${mode}`);
    } catch (err: any) {
      setMessage(`Failed to set fault mode: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleReset = async () => {
    setLoading(true);
    setMessage(null);
    try {
      const reset = await faultService.resetFaultMode();
      setFaultState(reset);
      setMessage('System restored to NORMAL operation.');
    } catch (err: any) {
      setMessage(`Failed to reset: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const modes = [
    { mode: 'NORMAL', label: 'Normal Baseline', desc: 'No faults. High success rate, low latency.', icon: CheckCircle, color: 'border-emerald-700 bg-emerald-950/20 text-emerald-400' },
    { mode: 'HIGH_LATENCY', label: 'High Latency Injection', desc: 'Injects bounded 2.5s request delays for P95 breach.', icon: Clock, color: 'border-amber-700 bg-amber-950/20 text-amber-400' },
    { mode: 'HTTP_ERRORS', label: 'HTTP 500 Error Storm', desc: 'Returns controlled HTTP 500 responses on 50% requests.', icon: ShieldAlert, color: 'border-red-700 bg-red-950/20 text-red-400' },
    { mode: 'FAILED_TRANSACTIONS', label: 'Transaction Failures', desc: 'Forces order placement transaction failures.', icon: AlertTriangle, color: 'border-orange-700 bg-orange-950/20 text-orange-400' },
    { mode: 'CPU_PRESSURE', label: 'CPU Load Stress', desc: 'Executes safe CPU computation spikes.', icon: Cpu, color: 'border-purple-700 bg-purple-950/20 text-purple-400' },
    { mode: 'MEMORY_PRESSURE', label: 'Memory Allocation Stress', desc: 'Simulates high memory allocation buffers.', icon: HardDrive, color: 'border-blue-700 bg-blue-950/20 text-blue-400' },
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
            <Flame className="w-7 h-7 text-amber-500" />
            <span>Fault Injection Experiment Lab</span>
          </h1>
          <p className="text-slate-400 text-sm">Controlled failure simulation for testing Argo Rollouts automatic rollback & Prometheus metric alerts</p>
        </div>
        <button
          onClick={handleReset}
          disabled={loading}
          className="flex items-center space-x-2 bg-slate-800 hover:bg-slate-700 text-slate-200 font-semibold px-4 py-2 rounded-xl border border-slate-700 transition"
        >
          <RefreshCw className="w-4 h-4" />
          <span>Reset to Normal</span>
        </button>
      </div>

      {faultState && (
        <div className="p-6 bg-slate-900 border border-slate-800 rounded-2xl flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
          <div>
            <span className="text-xs font-semibold uppercase text-slate-400 tracking-wider">Active Fault Mode</span>
            <div className="flex items-center space-x-3 mt-1">
              <span className={`text-xl font-bold ${faultState.active ? 'text-amber-400' : 'text-emerald-400'}`}>
                {faultState.activeMode}
              </span>
              <span className={`text-xs font-bold px-2.5 py-0.5 rounded-full ${
                faultState.active ? 'bg-amber-500/20 text-amber-300 border border-amber-500/30' : 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
              }`}>
                {faultState.active ? 'INJECTING FAULT' : 'BASELINE NORMAL'}
              </span>
            </div>
            <p className="text-sm text-slate-400 mt-1">{faultState.description}</p>
          </div>
          <div className="text-right text-xs text-slate-500 font-mono">
            <p>Prometheus Metric Tag: cloudcart.fault.active</p>
            <p>Last Updated: {new Date(faultState.updatedTime).toLocaleTimeString()}</p>
          </div>
        </div>
      )}

      {message && (
        <div className="p-4 bg-slate-800/80 border border-slate-700 rounded-xl text-sm text-slate-200 font-medium">
          {message}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {modes.map((m) => {
          const Icon = m.icon;
          const isActive = faultState?.activeMode === m.mode;
          return (
            <div
              key={m.mode}
              className={`p-6 rounded-2xl border transition flex flex-col justify-between space-y-4 ${m.color} ${
                isActive ? 'ring-2 ring-blue-500 shadow-xl' : 'hover:border-slate-700 bg-slate-900'
              }`}
            >
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Icon className="w-6 h-6" />
                  {isActive && <span className="text-xs font-bold uppercase bg-blue-600 text-white px-2 py-0.5 rounded">Active</span>}
                </div>
                <h3 className="font-bold text-slate-100 text-lg">{m.label}</h3>
                <p className="text-xs text-slate-400 leading-relaxed">{m.desc}</p>
              </div>

              <button
                onClick={() => handleSetMode(m.mode)}
                disabled={loading || isActive}
                className={`w-full py-2.5 rounded-xl font-semibold text-xs transition uppercase tracking-wider ${
                  isActive
                    ? 'bg-slate-800 text-slate-500 cursor-not-allowed'
                    : 'bg-blue-600 hover:bg-blue-500 text-white shadow-lg shadow-blue-600/20'
                }`}
              >
                {isActive ? 'Currently Active' : `Activate ${m.mode}`}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};
