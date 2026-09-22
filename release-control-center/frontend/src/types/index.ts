export interface Application {
  id: number;
  name: string;
  namespace: string;
  gitRepo: string;
  targetRevision: string;
  syncStatus: string;
  healthStatus: string;
  autoSync: boolean;
  selfHeal: boolean;
  updatedAt: string;
  pods?: PodInfo[];
}

export interface PodInfo {
  name: string;
  status: string;
  ready: boolean;
  restarts: number;
  type: string;
  version: string;
}

export interface Deployment {
  id: number;
  applicationName: string;
  version: string;
  environment: string;
  status: string;
  commitSha: string;
  commitMessage: string;
  author: string;
  startedAt: string;
  completedAt: string;
}

export interface MetricAnalysis {
  name: string;
  value: string;
  threshold: string;
  status: 'PASS' | 'FAIL' | 'WARNING';
}

export interface Rollout {
  id: number;
  applicationName: string;
  namespace: string;
  stableVersion: string;
  canaryVersion: string;
  currentWeight: number;
  currentStep: number;
  totalSteps: number;
  phase: string;
  analysisStatus: string;
  startedAt: string;
  updatedAt: string;
  metricsAnalysis: MetricAnalysis[];
}

export interface Metric {
  name: string;
  currentValue: string;
  threshold: string;
  status: 'PASS' | 'FAIL' | 'WARNING';
  trend: 'STABLE' | 'IMPROVING' | 'DEGRADING';
  lastChecked: string;
}

export interface PipelineRun {
  id: number;
  workflowName: string;
  commitSha: string;
  branch: string;
  author: string;
  status: string;
  conclusion: string;
  startedAt: string;
  durationSeconds: number;
  imageTag: string;
}

export interface GitOpsStatus {
  applicationName: string;
  gitRevision: string;
  desiredRevision: string;
  syncStatus: string;
  healthStatus: string;
  repositoryUrl: string;
  repositoryPath: string;
  namespace: string;
  cluster: string;
  lastSync: string;
  autoSync: boolean;
  selfHeal: boolean;
  prune: boolean;
}

export interface RollbackEvent {
  id: number;
  applicationName: string;
  failedVersion: string;
  restoredVersion: string;
  failureReason: string;
  failedMetric: string;
  measuredValue: string;
  thresholdValue: string;
  rolloutStage: string;
  trafficExposure: string;
  detectedAt: string;
  rollbackStartedAt: string;
  recoveredAt: string;
  recoveryDurationSeconds: number;
  automatic: boolean;
  status: string;
}

export interface DoraMetrics {
  period: string;
  deploymentFrequency: number;
  deploymentFrequencyRating: string;
  leadTimeHours: number;
  leadTimeRating: string;
  changeFailureRatePct: number;
  changeFailureRateRating: string;
  mttrMinutes: number;
  mttrRating: string;
  historicalTrend?: any;
}

export interface ActivityEvent {
  id: number;
  eventType: string;
  title: string;
  description: string;
  applicationName: string;
  severity: string;
  timestamp: string;
}

export interface Experiment {
  id: number;
  mode: string;
  description: string;
  active: boolean;
  startedAt: string;
  endedAt: string;
  status: string;
}

export interface DashboardSummary {
  heroMessage: string;
  totalApplications: number;
  healthyApplications: number;
  activeRollouts: number;
  failedDeployments: number;
  argoCdStatus: string;
  prometheusStatus: string;
  clusterHealth: any;
  activeRolloutSummary: Rollout;
  doraSummary: DoraMetrics;
  recentRollbacks: RollbackEvent[];
  recentActivity: ActivityEvent[];
}
