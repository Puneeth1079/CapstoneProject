import apiClient from './apiClient';
import {
  DashboardSummary, Application, Deployment, Rollout, Metric,
  PipelineRun, GitOpsStatus, RollbackEvent, DoraMetrics, ActivityEvent, Experiment
} from '../types';

export const devopsService = {
  getSummary: async (): Promise<DashboardSummary> => {
    const res = await apiClient.get('/dashboard/summary');
    return res.data;
  },
  getApplications: async (): Promise<Application[]> => {
    const res = await apiClient.get('/applications');
    return res.data;
  },
  getApplicationByName: async (name: string): Promise<Application> => {
    const res = await apiClient.get(`/applications/${name}`);
    return res.data;
  },
  getDeployments: async (): Promise<Deployment[]> => {
    const res = await apiClient.get('/deployments');
    return res.data;
  },
  getRollout: async (appName: string): Promise<Rollout> => {
    const res = await apiClient.get(`/rollouts/${appName}`);
    return res.data;
  },
  promoteRollout: async (appName: string): Promise<Rollout> => {
    const res = await apiClient.post(`/rollouts/${appName}/promote`);
    return res.data;
  },
  pauseRollout: async (appName: string): Promise<Rollout> => {
    const res = await apiClient.post(`/rollouts/${appName}/pause`);
    return res.data;
  },
  resumeRollout: async (appName: string): Promise<Rollout> => {
    const res = await apiClient.post(`/rollouts/${appName}/resume`);
    return res.data;
  },
  abortRollout: async (appName: string): Promise<Rollout> => {
    const res = await apiClient.post(`/rollouts/${appName}/abort`);
    return res.data;
  },
  retryRollout: async (appName: string): Promise<Rollout> => {
    const res = await apiClient.post(`/rollouts/${appName}/retry`);
    return res.data;
  },
  getMetrics: async (appName: string): Promise<Metric[]> => {
    const res = await apiClient.get(`/metrics/${appName}`);
    return res.data;
  },
  getPipelines: async (): Promise<PipelineRun[]> => {
    const res = await apiClient.get('/pipelines');
    return res.data;
  },
  getGitOpsApps: async (): Promise<GitOpsStatus[]> => {
    const res = await apiClient.get('/argocd/applications');
    return res.data;
  },
  getRollbackHistory: async (): Promise<RollbackEvent[]> => {
    const res = await apiClient.get('/rollbacks');
    return res.data;
  },
  getDoraMetrics: async (period = 'daily'): Promise<DoraMetrics> => {
    const res = await apiClient.get(`/dora?period=${period}`);
    return res.data;
  },
  getActivity: async (): Promise<ActivityEvent[]> => {
    const res = await apiClient.get('/activity');
    return res.data;
  },
  triggerExperiment: async (mode: string, description?: string): Promise<Experiment> => {
    const res = await apiClient.post('/experiments', { mode, description });
    return res.data;
  },
  getActiveExperiment: async (): Promise<Experiment> => {
    const res = await apiClient.get('/experiments/active');
    return res.data;
  },
  resetExperiment: async (): Promise<Experiment> => {
    const res = await apiClient.post('/experiments/reset');
    return res.data;
  }
};
