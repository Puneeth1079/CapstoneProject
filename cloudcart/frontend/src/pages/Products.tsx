import React, { useEffect, useState } from 'react';
import { productService } from '../services/productService';
import { Product } from '../types';
import { Package, Plus, Search } from 'lucide-react';

export const Products: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    productService.getAllProducts()
      .then(data => {
        setProducts(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  const filtered = products.filter(p => p.name.toLowerCase().includes(search.toLowerCase()) || p.sku.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Product Catalog</h1>
          <p className="text-slate-400 text-sm">Manage products and inventory pricing</p>
        </div>
        <div className="relative w-full sm:w-64">
          <Search className="w-4 h-4 absolute left-3 top-3 text-slate-400" />
          <input
            type="text"
            placeholder="Search products or SKU..."
            value={search}
            onChange={e => setSearch(e.target.value)}
            className="w-full bg-slate-900 border border-slate-800 rounded-xl pl-9 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:border-blue-500"
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {filtered.map(product => (
          <div key={product.id} className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden hover:border-slate-700 transition group">
            <div className="h-40 bg-slate-800 overflow-hidden relative">
              <img src={product.imageUrl} alt={product.name} className="w-full h-full object-cover group-hover:scale-105 transition duration-300" />
              <span className="absolute top-2 right-2 bg-slate-900/80 backdrop-blur-md text-xs font-semibold px-2.5 py-1 rounded-lg text-slate-300 border border-slate-700">
                {product.category}
              </span>
            </div>
            <div className="p-4 space-y-2">
              <h3 className="font-semibold text-slate-200 truncate">{product.name}</h3>
              <p className="text-xs text-slate-400 line-clamp-2">{product.description}</p>
              <div className="pt-2 flex justify-between items-center">
                <span className="text-lg font-bold text-blue-400">${product.price.toFixed(2)}</span>
                <span className="text-xs text-slate-400 font-mono bg-slate-800 px-2 py-0.5 rounded">{product.sku}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
