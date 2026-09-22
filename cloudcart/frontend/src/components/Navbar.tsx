import React from 'react';
import { ShoppingBag, Zap, ShieldCheck } from 'lucide-react';
import { Link } from 'react-router-dom';

interface NavbarProps {
  activeFaultMode?: string;
}

export const Navbar: React.FC<NavbarProps> = ({ activeFaultMode = 'NORMAL' }) => {
  const isFaulty = activeFaultMode !== 'NORMAL';

  return (
    <header className="bg-slate-900 border-b border-slate-800 sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="bg-blue-600 p-2 rounded-xl text-white shadow-lg shadow-blue-500/20">
            <ShoppingBag className="w-6 h-6" />
          </div>
          <div>
            <span className="text-xl font-bold bg-gradient-to-r from-blue-400 to-cyan-300 bg-clip-text text-transparent">
              CloudCart
            </span>
            <span className="ml-2 text-xs font-semibold px-2 py-0.5 rounded-full bg-slate-800 text-slate-400 border border-slate-700">
              v1.2.0
            </span>
          </div>
        </div>

        <div className="flex items-center space-x-4">
          <div className={`flex items-center space-x-2 px-3 py-1.5 rounded-lg text-xs font-semibold border ${
            isFaulty 
              ? 'bg-amber-950/40 text-amber-400 border-amber-800 animate-pulse' 
              : 'bg-emerald-950/40 text-emerald-400 border-emerald-800'
          }`}>
            <Zap className="w-4 h-4" />
            <span>Fault Mode: {activeFaultMode}</span>
          </div>

          <Link 
            to="/experiment" 
            className="flex items-center space-x-1.5 text-xs font-medium bg-slate-800 hover:bg-slate-700 text-slate-200 px-3 py-1.5 rounded-lg border border-slate-700 transition"
          >
            <ShieldCheck className="w-4 h-4 text-blue-400" />
            <span>Experiment Lab</span>
          </Link>
        </div>
      </div>
    </header>
  );
};
