import React, { useEffect, useState } from 'react';
import { devopsService } from '../services/devopsService';
import { Metric } from '../types';
import { LineChart as LineChartIcon, CheckCircle2, AlertTriangle, XCircle, TrendingUp, TrendingDown, Minus } from 'lucide-react';

export const Metrics: React.FC = () => {
  const [metrics, setMetrics] = useState<Metric[]>([]);

  useEffect(() => {
    fetchMetrics();
    const interval = setInterval(fetchMetrics, 3000);
    return () => clearInterval(interval);
  }, []);

  const fetchMetrics = () => {
    devopsService.getMetrics('cloudcart-backend').then(setMetrics).catch(console.error);
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100 flex items-center gap-2">
          <LineChartIcon className="w-7 h-7 text-blue-400" />
          <span>Prometheus Golden Signal Metrics</span>
        </h1>
        <p className="text-slate-400 text-sm">Real-time metrics scraped from CloudCart Spring Boot Actuator / Micrometer</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {metrics.map((m, idx) => (
          <div key={idx} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-xs font-semibold uppercase tracking-wider text-slate-400">{m.name}</p>
                <p className="text-3xl font-extrabold text-white mt-1">{m.currentValue}</p>
              </div>

              <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${
                m.status === 'PASS' ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' :
                m.status === 'WARNING' ? 'bg-amber-500/10 text-amber-400 border border-amber-500/20' :
                'bg-red-500/10 text-red-400 border border-red-500/20'
              }`}>
                {m.status}
              </span>
            </div>

            <div className="pt-3 border-t border-slate-800 flex justify-between items-center text-xs text-slate-400">
              <span>Threshold: <strong className="text-slate-200">{m.threshold}</strong></span>
              <span className="flex items-center space-x-1">
                {m.trend === 'DEGRADING' ? <TrendingDown className="w-4 h-4 text-red-400" /> : <TrendingUp className="w-4 h-4 text-emerald-400" />}
                <span className={m.trend === 'DEGRADING' ? 'text-red-400 font-semibold' : 'text-emerald-400 font-semibold'}>
                  {m.trend}
                </span>
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
