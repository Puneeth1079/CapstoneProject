import React, { useEffect, useState } from 'react';
import { orderService } from '../services/orderService';
import { DashboardSummary } from '../types';
import { Package, ShoppingCart, Users, DollarSign, AlertTriangle, Activity } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

export const Dashboard: React.FC = () => {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    orderService.getDashboardSummary()
      .then(data => {
        setSummary(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message || 'Failed to fetch storefront dashboard data');
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64 text-slate-400">
        <Activity className="w-8 h-8 animate-spin text-blue-500 mr-2" />
        <span>Loading CloudCart Metrics...</span>
      </div>
    );
  }

  if (error || !summary) {
    return (
      <div className="p-6 bg-red-950/40 border border-red-800 rounded-xl text-red-300">
        <p className="font-semibold">Error Loading Dashboard</p>
        <p className="text-sm">{error || 'Unable to connect to CloudCart backend API'}</p>
      </div>
    );
  }

  const statCards = [
    { title: 'Total Revenue', value: `$${summary.totalRevenue.toFixed(2)}`, icon: DollarSign, color: 'text-emerald-400', bg: 'bg-emerald-500/10' },
    { title: 'Total Orders', value: summary.totalOrders, icon: ShoppingCart, color: 'text-blue-400', bg: 'bg-blue-500/10' },
    { title: 'Total Products', value: summary.totalProducts, icon: Package, color: 'text-purple-400', bg: 'bg-purple-500/10' },
    { title: 'Low Stock Items', value: summary.lowStockCount, icon: AlertTriangle, color: summary.lowStockCount > 0 ? 'text-amber-400' : 'text-slate-400', bg: 'bg-amber-500/10' },
  ];

  const chartData = [
    { name: 'Mon', revenue: 420 },
    { name: 'Tue', revenue: 680 },
    { name: 'Wed', revenue: 950 },
    { name: 'Thu', revenue: 1200 },
    { name: 'Fri', revenue: 850 },
    { name: 'Sat', revenue: 1400 },
    { name: 'Sun', revenue: summary.totalRevenue || 1100 },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">CloudCart Store Dashboard</h1>
        <p className="text-slate-400 text-sm">Real-time e-commerce operational state & business metrics</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {statCards.map((card, idx) => {
          const Icon = card.icon;
          return (
            <div key={idx} className="bg-slate-900 border border-slate-800 p-5 rounded-2xl flex items-center justify-between">
              <div>
                <p className="text-slate-400 text-xs font-medium uppercase tracking-wider">{card.title}</p>
                <p className="text-2xl font-bold text-slate-100 mt-1">{card.value}</p>
              </div>
              <div className={`p-3 rounded-xl ${card.bg} ${card.color}`}>
                <Icon className="w-6 h-6" />
              </div>
            </div>
          );
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
          <h2 className="text-lg font-semibold text-slate-200">Weekly Revenue Breakdown</h2>
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={chartData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                <XAxis dataKey="name" stroke="#94a3b8" />
                <YAxis stroke="#94a3b8" />
                <Tooltip contentStyle={{ backgroundColor: '#1e293b', borderColor: '#475569', borderRadius: '0.75rem' }} />
                <Bar dataKey="revenue" fill="#3b82f6" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
          <h2 className="text-lg font-semibold text-slate-200">Recent Orders</h2>
          <div className="space-y-3">
            {summary.recentOrders.slice(0, 5).map(order => (
              <div key={order.id} className="p-3 bg-slate-800/60 rounded-xl border border-slate-700/50 flex justify-between items-center">
                <div>
                  <p className="text-sm font-semibold text-slate-200">{order.orderNumber}</p>
                  <p className="text-xs text-slate-400">{order.customerName}</p>
                </div>
                <div className="text-right">
                  <p className="text-sm font-bold text-emerald-400">${order.totalAmount.toFixed(2)}</p>
                  <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300">
                    {order.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
