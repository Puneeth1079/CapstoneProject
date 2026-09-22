import apiClient from './apiClient';
import { Product } from '../types';

export const productService = {
  getAllProducts: async (category?: string): Promise<Product[]> => {
    const response = await apiClient.get('/products', { params: { category } });
    return response.data;
  },
  getProductById: async (id: number): Promise<Product> => {
    const response = await apiClient.get(`/products/${id}`);
    return response.data;
  },
  createProduct: async (productData: Partial<Product>): Promise<Product> => {
    const response = await apiClient.post('/products', productData);
    return response.data;
  },
};
