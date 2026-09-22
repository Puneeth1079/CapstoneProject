package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.RolloutRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolloutRecordRepository extends JpaRepository<RolloutRecord, Long> {
    Optional<RolloutRecord> findByApplicationName(String applicationName);
    long countByPhase(String phase);
}
