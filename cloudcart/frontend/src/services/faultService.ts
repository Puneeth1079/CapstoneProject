import apiClient from './apiClient';
import { FaultModeState } from '../types';

export const faultService = {
  getFaultMode: async (): Promise<FaultModeState> => {
    const response = await apiClient.get('/demo/fault-mode');
    return response.data;
  },
  setFaultMode: async (mode: string, errorRate?: number, latencyMs?: number): Promise<FaultModeState> => {
    const response = await apiClient.post('/demo/fault-mode', { mode, errorRate, latencyMs });
    return response.data;
  },
  resetFaultMode: async (): Promise<FaultModeState> => {
    const response = await apiClient.post('/demo/fault-mode/reset');
    return response.data;
  }
};
