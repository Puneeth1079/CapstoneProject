import apiClient from './apiClient';
import { Customer } from '../types';

export const customerService = {
  getAllCustomers: async (): Promise<Customer[]> => {
    const response = await apiClient.get('/customers');
    return response.data;
  },
  createCustomer: async (data: Partial<Customer>): Promise<Customer> => {
    const response = await apiClient.post('/customers', data);
    return response.data;
  }
};
