package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.DeploymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeploymentRepository extends JpaRepository<DeploymentEntity, Long> {
    List<DeploymentEntity> findByApplicationNameOrderByStartedAtDesc(String applicationName);
    List<DeploymentEntity> findTop10ByOrderByStartedAtDesc();
    long countByStatus(String status);
}
