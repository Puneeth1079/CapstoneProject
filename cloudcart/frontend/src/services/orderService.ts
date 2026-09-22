import apiClient from './apiClient';
import { Order, DashboardSummary, InventoryItem } from '../types';

export const orderService = {
  createOrder: async (customerId: number, items: { productId: number; quantity: number }[]): Promise<Order> => {
    const response = await apiClient.post('/orders', { customerId, items });
    return response.data;
  },
  getAllOrders: async (): Promise<Order[]> => {
    const response = await apiClient.get('/orders');
    return response.data;
  },
  getDashboardSummary: async (): Promise<DashboardSummary> => {
    const response = await apiClient.get('/orders/dashboard/summary');
    return response.data;
  },
  getInventory: async (): Promise<InventoryItem[]> => {
    const response = await apiClient.get('/inventory');
    return response.data;
  }
};
