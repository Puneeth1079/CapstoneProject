CREATE TABLE IF NOT EXISTS applications (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    namespace VARCHAR(100) NOT NULL DEFAULT 'default',
    git_repo VARCHAR(500) NOT NULL,
    target_revision VARCHAR(100) NOT NULL DEFAULT 'HEAD',
    sync_status VARCHAR(50) NOT NULL DEFAULT 'Synced',
    health_status VARCHAR(50) NOT NULL DEFAULT 'Healthy',
    auto_sync BOOLEAN NOT NULL DEFAULT true,
    self_heal BOOLEAN NOT NULL DEFAULT true,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS deployments (
    id BIGSERIAL PRIMARY KEY,
    application_name VARCHAR(255) NOT NULL,
    version VARCHAR(100) NOT NULL,
    environment VARCHAR(50) NOT NULL DEFAULT 'production',
    status VARCHAR(50) NOT NULL DEFAULT 'SUCCESS',
    commit_sha VARCHAR(100) NOT NULL,
    commit_message TEXT,
    author VARCHAR(255),
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS rollout_records (
    id BIGSERIAL PRIMARY KEY,
    application_name VARCHAR(255) NOT NULL UNIQUE,
    namespace VARCHAR(100) NOT NULL DEFAULT 'default',
    stable_version VARCHAR(100) NOT NULL,
    canary_version VARCHAR(100),
    current_weight INT NOT NULL DEFAULT 100,
    current_step INT NOT NULL DEFAULT 0,
    phase VARCHAR(50) NOT NULL DEFAULT 'Healthy',
    analysis_status VARCHAR(50) NOT NULL DEFAULT 'Passed',
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pipeline_runs (
    id BIGSERIAL PRIMARY KEY,
    workflow_name VARCHAR(255) NOT NULL,
    commit_sha VARCHAR(100) NOT NULL,
    branch VARCHAR(100) NOT NULL DEFAULT 'main',
    author VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'completed',
    conclusion VARCHAR(50) NOT NULL DEFAULT 'success',
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    duration_seconds INT NOT NULL DEFAULT 120,
    image_tag VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS rollback_events (
    id BIGSERIAL PRIMARY KEY,
    application_name VARCHAR(255) NOT NULL,
    failed_version VARCHAR(100) NOT NULL,
    restored_version VARCHAR(100) NOT NULL,
    failure_reason TEXT NOT NULL,
    failed_metric VARCHAR(100) NOT NULL,
    measured_value VARCHAR(100) NOT NULL,
    threshold_value VARCHAR(100) NOT NULL,
    rollout_stage VARCHAR(100) NOT NULL,
    traffic_exposure VARCHAR(50) NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    rollback_started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    recovered_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    recovery_duration_seconds INT NOT NULL DEFAULT 45,
    automatic BOOLEAN NOT NULL DEFAULT true,
    status VARCHAR(50) NOT NULL DEFAULT 'COMPLETED'
);

CREATE TABLE IF NOT EXISTS activity_events (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    application_name VARCHAR(255),
    severity VARCHAR(50) NOT NULL DEFAULT 'INFO',
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dora_metric_snapshots (
    id BIGSERIAL PRIMARY KEY,
    period_type VARCHAR(50) NOT NULL DEFAULT 'daily',
    deployment_frequency DOUBLE PRECISION NOT NULL DEFAULT 4.2,
    lead_time_hours DOUBLE PRECISION NOT NULL DEFAULT 1.5,
    change_failure_rate_pct DOUBLE PRECISION NOT NULL DEFAULT 2.1,
    mttr_minutes DOUBLE PRECISION NOT NULL DEFAULT 8.5,
    snapshot_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS release_experiments (
    id BIGSERIAL PRIMARY KEY,
    mode VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT false,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL DEFAULT 'COMPLETED'
);

-- Seed Initial Data for Demonstration
INSERT INTO applications (id, name, namespace, git_repo, target_revision, sync_status, health_status, auto_sync, self_heal) VALUES
(1, 'cloudcart-backend', 'default', 'https://github.com/capstone-org/capstone-gitops-platform', 'main', 'Synced', 'Healthy', true, true),
(2, 'cloudcart-frontend', 'default', 'https://github.com/capstone-org/capstone-gitops-platform', 'main', 'Synced', 'Healthy', true, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO rollout_records (id, application_name, namespace, stable_version, canary_version, current_weight, current_step, phase, analysis_status) VALUES
(1, 'cloudcart-backend', 'default', 'cloudcart-backend:v1.2.0', 'cloudcart-backend:a82fc31', 10, 1, 'Progressing', 'Evaluating')
ON CONFLICT (id) DO NOTHING;

INSERT INTO dora_metric_snapshots (id, period_type, deployment_frequency, lead_time_hours, change_failure_rate_pct, mttr_minutes) VALUES
(1, 'daily', 5.0, 1.2, 0.0, 5.0),
(2, 'weekly', 3.8, 1.8, 4.2, 12.4),
(3, 'monthly', 4.1, 1.5, 2.5, 9.1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO rollback_events (id, application_name, failed_version, restored_version, failure_reason, failed_metric, measured_value, threshold_value, rollout_stage, traffic_exposure, recovery_duration_seconds, automatic, status) VALUES
(1, 'cloudcart-backend', 'cloudcart-backend:v1.3.0-rc1', 'cloudcart-backend:v1.2.0', 'Prometheus AnalysisTemplate threshold breach: HTTP Error Rate > 2%', 'HTTP Error Rate', '8.4%', '<= 2.0%', 'Canary Step 2 (25%)', '25%', 38, true, 'COMPLETED')
ON CONFLICT (id) DO NOTHING;

INSERT INTO activity_events (id, event_type, title, description, application_name, severity) VALUES
(1, 'GITOPS_SYNC', 'Argo CD Synchronized', 'CloudCart backend synchronized to commit a82fc31', 'cloudcart-backend', 'INFO'),
(2, 'CANARY_START', 'Canary Rollout Triggered', 'Promoted to 10% canary traffic', 'cloudcart-backend', 'INFO'),
(3, 'METRIC_ANALYSIS', 'Prometheus Metrics Normal', 'HTTP Success Rate 99.8%, P95 Latency 120ms', 'cloudcart-backend', 'INFO')
ON CONFLICT (id) DO NOTHING;

SELECT setval('applications_id_seq', (SELECT MAX(id) FROM applications));
SELECT setval('deployments_id_seq', (SELECT COALESCE(MAX(id), 1) FROM deployments));
SELECT setval('rollout_records_id_seq', (SELECT MAX(id) FROM rollout_records));
SELECT setval('rollback_events_id_seq', (SELECT MAX(id) FROM rollback_events));
SELECT setval('activity_events_id_seq', (SELECT MAX(id) FROM activity_events));
SELECT setval('dora_metric_snapshots_id_seq', (SELECT MAX(id) FROM dora_metric_snapshots));
SELECT setval('release_experiments_id_seq', (SELECT COALESCE(MAX(id), 1) FROM release_experiments));
