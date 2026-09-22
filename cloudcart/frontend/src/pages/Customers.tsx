import React, { useEffect, useState } from 'react';
import { customerService } from '../services/customerService';
import { Customer } from '../types';
import { Users, Mail, Phone, MapPin } from 'lucide-react';

export const Customers: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);

  useEffect(() => {
    customerService.getAllCustomers()
      .then(setCustomers)
      .catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">Customer Profiles</h1>
        <p className="text-slate-400 text-sm">Registered buyers and order history associations</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {customers.map(c => (
          <div key={c.id} className="bg-slate-900 border border-slate-800 p-6 rounded-2xl space-y-4">
            <div className="flex items-center space-x-3">
              <div className="w-10 h-10 rounded-full bg-blue-600/20 text-blue-400 flex items-center justify-center font-bold text-lg">
                {c.name.charAt(0)}
              </div>
              <div>
                <h3 className="font-semibold text-slate-200">{c.name}</h3>
                <p className="text-xs text-slate-400">ID #{c.id}</p>
              </div>
            </div>

            <div className="space-y-2 text-xs text-slate-300 pt-2 border-t border-slate-800">
              <div className="flex items-center space-x-2">
                <Mail className="w-4 h-4 text-slate-400" />
                <span>{c.email}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Phone className="w-4 h-4 text-slate-400" />
                <span>{c.phone || '+1-555-0192'}</span>
              </div>
              <div className="flex items-center space-x-2">
                <MapPin className="w-4 h-4 text-slate-400" />
                <span>{c.city}, {c.country}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
