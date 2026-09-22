import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { DoraMetrics } from '../types';
import { Gauge, Calendar, TrendingUp, ShieldCheck, Clock, RefreshCcw } from 'lucide-react';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

export const DoraMetricsPage: React.FC = () => {
  const [dora, setDora] = useState<DoraMetrics | null>(null);
  const [period, setPeriod] = useState('daily');

  useEffect(() => {
    devopsService.getDoraMetrics(period).then(setDora).catch(console.error);
  }, [period]);

  if (!dora) {
    return <div className="p-8 text-slate-400">Loading DORA Metrics Engine...</div>;
  }

  const metricsList = [
    { title: 'Deployment Frequency', val: `${dora.deploymentFrequency.toFixed(1)} / day`, rating: dora.deploymentFrequencyRating, icon: TrendingUp, color: 'text-blue-400', desc: 'Successful production deployments count' },
    { title: 'Lead Time for Changes', val: `${dora.leadTimeHours.toFixed(1)} hours`, rating: dora.leadTimeRating, icon: Clock, color: 'text-cyan-400', desc: 'Commit created → running in production' },
    { title: 'Change Failure Rate', val: `${dora.changeFailureRatePct.toFixed(1)}%`, rating: dora.changeFailureRateRating, icon: RefreshCcw, color: 'text-amber-400', desc: 'Failed releases / total deployments' },
    { title: 'Mean Time to Recovery', val: `${dora.mttrMinutes.toFixed(1)} mins`, rating: dora.mttrRating, icon: ShieldCheck, color: 'text-emerald-400', desc: 'Failure detected → healthy release restored' },
  ];

  const trendData = [
    { day: 'Mon', frequency: 4, failureRate: 0, mttr: 5 },
    { day: 'Tue', frequency: 5, failureRate: 0, mttr: 6 },
    { day: 'Wed', frequency: 3, failureRate: 3.2, mttr: 12 },
    { day: 'Thu', frequency: 6, failureRate: 0, mttr: 4 },
    { day: 'Fri', frequency: 4, failureRate: 1.8, mttr: 8 },
    { day: 'Sat', frequency: 5, failureRate: 0, mttr: 5 },
    { day: 'Sun', frequency: dora.deploymentFrequency, failureRate: dora.changeFailureRatePct, mttr: dora.mttrMinutes },
  ];

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
            <Gauge className="w-7 h-7 text-purple-400" />
            <span>DORA Software Delivery Performance Metrics</span>
          </h1>
          <p className="text-slate-400 text-sm">Industry-standard DevOps delivery throughput and stability indicators</p>
        </div>

        <div className="flex bg-slate-900 p-1 rounded-xl border border-slate-800 text-xs">
          {['daily', 'weekly', 'monthly'].map(p => (
            <button
              key={p}
              onClick={() => setPeriod(p)}
              className={`px-3 py-1.5 rounded-lg capitalize font-semibold transition ${
                period === p ? 'bg-blue-600 text-white shadow-md' : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              {p}
            </button>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {metricsList.map((m, idx) => {
          const Icon = m.icon;
          return (
            <div key={idx} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
              <div className="flex justify-between items-start">
                <div className={`p-3 rounded-xl bg-slate-800 ${m.color}`}>
                  <Icon className="w-6 h-6" />
                </div>
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 uppercase">
                  {m.rating}
                </span>
              </div>

              <div>
                <p className="text-xs text-slate-400 uppercase font-semibold">{m.title}</p>
                <p className="text-2xl font-extrabold text-white mt-1">{m.val}</p>
                <p className="text-[11px] text-slate-500 mt-1">{m.desc}</p>
              </div>
            </div>
          );
        })}
      </div>

      <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
        <h2 className="text-lg font-bold text-slate-100">Historical Deployment & Stability Trends</h2>
        <div className="h-72">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={trendData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
              <XAxis dataKey="day" stroke="#94a3b8" />
              <YAxis stroke="#94a3b8" />
              <Tooltip contentStyle={{ backgroundColor: '#1e293b', borderColor: '#475569', borderRadius: '0.75rem' }} />
              <Line type="monotone" dataKey="frequency" stroke="#3b82f6" strokeWidth={3} name="Deployments / Day" />
              <Line type="monotone" dataKey="failureRate" stroke="#f59e0b" strokeWidth={2} name="Change Failure Rate %" />
              <Line type="monotone" dataKey="mttr" stroke="#10b981" strokeWidth={2} name="MTTR (mins)" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};
