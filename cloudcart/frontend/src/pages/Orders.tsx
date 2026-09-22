import React, { useEffect, useState } from 'react';
import { orderService } from '../services/orderService';
import { productService } from '../services/productService';
import { Order, Product } from '../types';
import { ShoppingCart, Plus, CheckCircle2, AlertCircle } from 'lucide-react';

export const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [creating, setCreating] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = () => {
    Promise.all([orderService.getAllOrders(), productService.getAllProducts()])
      .then(([ordersData, productsData]) => {
        setOrders(ordersData);
        setProducts(productsData);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  };

  const handleSimulateOrder = async () => {
    if (products.length === 0) return;
    setCreating(true);
    setMessage(null);

    try {
      const selectedProduct = products[0];
      await orderService.createOrder(1, [{ productId: selectedProduct.id, quantity: 1 }]);
      setMessage('Order created successfully!');
      loadData();
    } catch (err: any) {
      setMessage(`Order Placement Failed: ${err.response?.data?.message || err.message}`);
    } finally {
      setCreating(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Orders Management</h1>
          <p className="text-slate-400 text-sm">Track customer transactions and placement status</p>
        </div>
        <button
          onClick={handleSimulateOrder}
          disabled={creating}
          className="flex items-center space-x-2 bg-blue-600 hover:bg-blue-500 text-white font-semibold px-4 py-2 rounded-xl transition shadow-lg shadow-blue-600/20 disabled:opacity-50"
        >
          <Plus className="w-4 h-4" />
          <span>{creating ? 'Processing...' : 'Simulate Order Placement'}</span>
        </button>
      </div>

      {message && (
        <div className={`p-4 rounded-xl border flex items-center space-x-3 ${
          message.includes('Failed')
            ? 'bg-red-950/40 border-red-800 text-red-300'
            : 'bg-emerald-950/40 border-emerald-800 text-emerald-300'
        }`}>
          {message.includes('Failed') ? <AlertCircle className="w-5 h-5 shrink-0" /> : <CheckCircle2 className="w-5 h-5 shrink-0" />}
          <span className="text-sm font-medium">{message}</span>
        </div>
      )}

      <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm text-slate-300">
            <thead className="bg-slate-800/80 text-xs font-semibold text-slate-400 uppercase tracking-wider">
              <tr>
                <th className="px-6 py-4">Order Number</th>
                <th className="px-6 py-4">Customer</th>
                <th className="px-6 py-4">Total Amount</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Items Count</th>
                <th className="px-6 py-4">Date</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800">
              {orders.map(order => (
                <tr key={order.id} className="hover:bg-slate-800/40 transition">
                  <td className="px-6 py-4 font-mono font-semibold text-blue-400">{order.orderNumber}</td>
                  <td className="px-6 py-4 font-medium text-slate-200">{order.customerName}</td>
                  <td className="px-6 py-4 font-bold text-emerald-400">${order.totalAmount.toFixed(2)}</td>
                  <td className="px-6 py-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${
                      order.status === 'COMPLETED' ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-amber-500/10 text-amber-400 border border-amber-500/20'
                    }`}>
                      {order.status}
                    </span>
                  </td>
                  <td className="px-6 py-4">{order.items?.length || 1} item(s)</td>
                  <td className="px-6 py-4 text-xs text-slate-400">{new Date(order.createdAt).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
