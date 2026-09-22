import React, { useEffect, useState } from 'react';
import { orderService } from '../services/orderService';
import { InventoryItem } from '../types';
import { Warehouse, AlertTriangle, CheckCircle2 } from 'lucide-react';

export const Inventory: React.FC = () => {
  const [items, setItems] = useState<InventoryItem[]>([]);

  useEffect(() => {
    orderService.getInventory()
      .then(setItems)
      .catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Stock & Inventory Management</h1>
        <p className="text-slate-400 text-sm">Real-time stock tracking and minimum threshold alerts</p>
      </div>

      <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
        <table className="w-full text-left text-sm text-slate-300">
          <thead className="bg-slate-800/80 text-xs font-semibold text-slate-400 uppercase tracking-wider">
            <tr>
              <th className="px-6 py-4">Product Name</th>
              <th className="px-6 py-4">SKU</th>
              <th className="px-6 py-4">Total Stock</th>
              <th className="px-6 py-4">Reserved</th>
              <th className="px-6 py-4">Available</th>
              <th className="px-6 py-4">Stock Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800">
            {items.map(item => (
              <tr key={item.id} className="hover:bg-slate-800/40 transition">
                <td className="px-6 py-4 font-semibold text-slate-200">{item.productName}</td>
                <td className="px-6 py-4 font-mono text-xs text-slate-400">{item.productSku}</td>
                <td className="px-6 py-4">{item.quantity}</td>
                <td className="px-6 py-4 text-slate-400">{item.reservedQuantity}</td>
                <td className="px-6 py-4 font-bold text-blue-400">{item.availableQuantity}</td>
                <td className="px-6 py-4">
                  {item.lowStock ? (
                    <span className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-amber-500/10 text-amber-400 border border-amber-500/20">
                      <AlertTriangle className="w-3.5 h-3.5" />
                      <span>Low Stock Alert</span>
                    </span>
                  ) : (
                    <span className="inline-flex items-center space-x-1 px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                      <CheckCircle2 className="w-3.5 h-3.5" />
                      <span>Healthy Stock</span>
                    </span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
