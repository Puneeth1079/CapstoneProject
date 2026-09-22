import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { DashboardSummary } from '../types';
import { Server, Activity, RefreshCcw, ShieldCheck, ArrowUpRight, Gauge, Layers, CheckCircle2, AlertTriangle, XCircle } from 'lucide-react';
import { Link } from 'react-router-dom';

export const Overview: React.FC = () => {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    devopsService.getSummary()
      .then(data => {
        setSummary(data);
        setLoading(false);
      })
      .catch(err => {
        console.error('Error loading dashboard summary:', err);
        setLoading(false);
      });
  }, []);

  if (loading || !summary) {
    return (
      <div className="flex items-center justify-center h-64 text-slate-400 space-x-2">
        <Activity className="w-6 h-6 animate-spin text-blue-500" />
        <span>Fetching live release control metrics...</span>
      </div>
    );
  }

  const statCards = [
    { label: 'Total Managed Apps', val: summary.totalApplications, icon: Server, color: 'text-blue-400', bg: 'bg-blue-500/10' },
    { label: 'Healthy Applications', val: summary.healthyApplications, icon: ShieldCheck, color: 'text-emerald-400', bg: 'bg-emerald-500/10' },
    { label: 'Active Rollouts', val: summary.activeRollouts, icon: Layers, color: 'text-amber-400', bg: 'bg-amber-500/10' },
    { label: 'Aborted / Failed', val: summary.failedDeployments, icon: RefreshCcw, color: summary.failedDeployments > 0 ? 'text-red-400' : 'text-slate-400', bg: 'bg-red-500/10' },
  ];

  return (
    <div className="space-y-6">
      {/* Hero Banner */}
      <div className="p-6 rounded-2xl bg-gradient-to-r from-blue-950 via-slate-900 to-slate-950 border border-blue-800/40 relative overflow-hidden shadow-2xl">
        <div className="relative z-10 max-w-3xl space-y-2">
          <span className="text-xs font-bold uppercase tracking-widest text-blue-400 bg-blue-500/10 px-3 py-1 rounded-full border border-blue-500/20">
            GitOps Release Orchestration Platform
          </span>
          <h1 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight">
            {summary.heroMessage}
          </h1>
          <p className="text-xs sm:text-sm text-slate-300 leading-relaxed">
            Real-time progressive delivery engine connected with Argo CD, Argo Rollouts, Prometheus metrics analysis, and automated canary rollbacks.
          </p>
        </div>
      </div>

      {/* Primary KPI Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {statCards.map((c, i) => {
          const Icon = c.icon;
          return (
            <div key={i} className="bg-slate-900 border border-slate-800 p-5 rounded-2xl flex items-center justify-between shadow-sm">
              <div>
                <p className="text-slate-400 text-xs font-semibold uppercase tracking-wider">{c.label}</p>
                <p className="text-2xl font-bold text-slate-100 mt-1">{c.val}</p>
              </div>
              <div className={`p-3 rounded-xl ${c.bg} ${c.color}`}>
                <Icon className="w-6 h-6" />
              </div>
            </div>
          );
        })}
      </div>

      {/* Active Rollout & DORA Summary Cards */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Active Rollout Visual Card */}
        <div className="lg:col-span-2 bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-5">
          <div className="flex justify-between items-center">
            <div>
              <h2 className="text-lg font-bold text-slate-100 flex items-center gap-2">
                <Layers className="w-5 h-5 text-blue-400" />
                <span>Active Canary Rollout (CloudCart Backend)</span>
              </h2>
              <p className="text-xs text-slate-400">Argo Rollout Strategy: Canary (10% → 25% → 50% → 75% → 100%)</p>
            </div>
            <Link to="/progressive-delivery" className="text-xs font-semibold text-blue-400 hover:text-blue-300 flex items-center gap-1">
              <span>View Controls</span>
              <ArrowUpRight className="w-4 h-4" />
            </Link>
          </div>

          {summary.activeRolloutSummary && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 bg-slate-950 p-4 rounded-xl border border-slate-800/80 text-xs">
                <div>
                  <span className="text-slate-500 font-medium">Stable Version</span>
                  <p className="font-mono font-bold text-emerald-400 mt-0.5">{summary.activeRolloutSummary.stableVersion}</p>
                </div>
                <div>
                  <span className="text-slate-500 font-medium">Candidate Version</span>
                  <p className="font-mono font-bold text-cyan-400 mt-0.5">{summary.activeRolloutSummary.canaryVersion || 'N/A'}</p>
                </div>
                <div>
                  <span className="text-slate-500 font-medium">Current Weight</span>
                  <p className="font-bold text-blue-400 text-sm mt-0.5">{summary.activeRolloutSummary.currentWeight}% Traffic</p>
                </div>
                <div>
                  <span className="text-slate-500 font-medium">Analysis Status</span>
                  <p className={`font-bold mt-0.5 ${summary.activeRolloutSummary.analysisStatus === 'Failed' ? 'text-red-400' : 'text-emerald-400'}`}>
                    {summary.activeRolloutSummary.analysisStatus}
                  </p>
                </div>
              </div>

              {/* Canary Traffic Visual Progress Bar */}
              <div className="space-y-1.5">
                <div className="flex justify-between text-xs font-semibold text-slate-300">
                  <span>Canary Progression</span>
                  <span>{summary.activeRolloutSummary.currentWeight}% Canary Traffic</span>
                </div>
                <div className="w-full h-3 bg-slate-800 rounded-full overflow-hidden flex">
                  <div
                    className="bg-emerald-500 h-full transition-all duration-500"
                    style={{ width: `${100 - summary.activeRolloutSummary.currentWeight}%` }}
                    title="Stable Traffic"
                  />
                  <div
                    className="bg-cyan-400 h-full transition-all duration-500"
                    style={{ width: `${summary.activeRolloutSummary.currentWeight}%` }}
                    title="Canary Candidate Traffic"
                  />
                </div>
                <div className="flex justify-between text-[11px] text-slate-500 font-mono">
                  <span>Stable Release (90%)</span>
                  <span>Candidate Canary (10%)</span>
                </div>
              </div>

              {/* Prometheus Analysis Template Checks */}
              <div className="space-y-2 pt-2">
                <h3 className="text-xs font-bold uppercase tracking-wider text-slate-400">Prometheus Metric AnalysisTemplate Thresholds</h3>
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
                  {summary.activeRolloutSummary.metricsAnalysis?.map((m: any, idx: number) => (
                    <div key={idx} className="p-3 bg-slate-950 border border-slate-800 rounded-xl flex items-center justify-between text-xs">
                      <div>
                        <p className="font-medium text-slate-300">{m.name}</p>
                        <p className="font-bold text-white mt-0.5">{m.value} <span className="text-slate-500 font-normal">({m.threshold})</span></p>
                      </div>
                      {m.status === 'PASS' ? (
                        <CheckCircle2 className="w-5 h-5 text-emerald-400 shrink-0" />
                      ) : (
                        <XCircle className="w-5 h-5 text-red-400 shrink-0" />
                      )}
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}
        </div>

        {/* DORA Metrics Overview Card */}
        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
          <div className="flex justify-between items-center">
            <h2 className="text-lg font-bold text-slate-100 flex items-center gap-2">
              <Gauge className="w-5 h-5 text-purple-400" />
              <span>DORA Metrics</span>
            </h2>
            <Link to="/dora-metrics" className="text-xs font-semibold text-purple-400 hover:text-purple-300">
              Details
            </Link>
          </div>

          {summary.doraSummary && (
            <div className="space-y-3">
              <div className="p-3.5 bg-slate-950 border border-slate-800/80 rounded-xl flex justify-between items-center">
                <div>
                  <p className="text-xs text-slate-400">Deployment Frequency</p>
                  <p className="text-lg font-bold text-white">{summary.doraSummary.deploymentFrequency.toFixed(1)} / day</p>
                </div>
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 uppercase">
                  {summary.doraSummary.deploymentFrequencyRating}
                </span>
              </div>

              <div className="p-3.5 bg-slate-950 border border-slate-800/80 rounded-xl flex justify-between items-center">
                <div>
                  <p className="text-xs text-slate-400">Lead Time for Changes</p>
                  <p className="text-lg font-bold text-white">{summary.doraSummary.leadTimeHours.toFixed(1)} hours</p>
                </div>
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 uppercase">
                  {summary.doraSummary.leadTimeRating}
                </span>
              </div>

              <div className="p-3.5 bg-slate-950 border border-slate-800/80 rounded-xl flex justify-between items-center">
                <div>
                  <p className="text-xs text-slate-400">Change Failure Rate</p>
                  <p className="text-lg font-bold text-white">{summary.doraSummary.changeFailureRatePct.toFixed(1)}%</p>
                </div>
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 uppercase">
                  {summary.doraSummary.changeFailureRateRating}
                </span>
              </div>

              <div className="p-3.5 bg-slate-950 border border-slate-800/80 rounded-xl flex justify-between items-center">
                <div>
                  <p className="text-xs text-slate-400">Mean Time to Recovery</p>
                  <p className="text-lg font-bold text-white">{summary.doraSummary.mttrMinutes.toFixed(1)} mins</p>
                </div>
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 uppercase">
                  {summary.doraSummary.mttrRating}
                </span>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Recent Rollbacks & Activity Stream */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
          <div className="flex justify-between items-center">
            <h2 className="text-lg font-bold text-slate-100 flex items-center gap-2">
              <RefreshCcw className="w-5 h-5 text-amber-400" />
              <span>Recent Automated Rollbacks</span>
            </h2>
            <Link to="/rollback-history" className="text-xs font-semibold text-amber-400 hover:text-amber-300">
              History Log
            </Link>
          </div>

          <div className="space-y-3">
            {summary.recentRollbacks.slice(0, 3).map(rb => (
              <div key={rb.id} className="p-4 bg-slate-950 border border-slate-800 rounded-xl space-y-2">
                <div className="flex justify-between items-start">
                  <div>
                    <span className="text-xs font-mono font-bold text-red-400">{rb.failedVersion}</span>
                    <span className="text-xs text-slate-500 mx-2">→</span>
                    <span className="text-xs font-mono font-bold text-emerald-400">{rb.restoredVersion}</span>
                  </div>
                  <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-red-500/20 text-red-300 border border-red-500/30">
                    AUTO ROLLBACK
                  </span>
                </div>
                <p className="text-xs text-slate-300 font-medium">{rb.failureReason}</p>
                <div className="flex justify-between text-[11px] text-slate-500 font-mono pt-1">
                  <span>Metric: {rb.failedMetric} ({rb.measuredValue})</span>
                  <span>Recovery Time: {rb.recoveryDurationSeconds}s</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
          <h2 className="text-lg font-bold text-slate-100 flex items-center gap-2">
            <Activity className="w-5 h-5 text-cyan-400" />
            <span>Platform Activity Stream</span>
          </h2>

          <div className="space-y-3">
            {summary.recentActivity.slice(0, 4).map(act => (
              <div key={act.id} className="p-3.5 bg-slate-950 border border-slate-800 rounded-xl flex items-start space-x-3">
                <div className="w-2 h-2 rounded-full bg-cyan-400 mt-2 shrink-0 animate-pulse" />
                <div className="flex-1 space-y-0.5">
                  <div className="flex justify-between items-center">
                    <span className="text-xs font-bold text-slate-200">{act.title}</span>
                    <span className="text-[10px] text-slate-500">{new Date(act.timestamp).toLocaleTimeString()}</span>
                  </div>
                  <p className="text-xs text-slate-400">{act.description}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
