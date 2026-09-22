package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.DoraMetricSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoraMetricSnapshotRepository extends JpaRepository<DoraMetricSnapshot, Long> {
    Optional<DoraMetricSnapshot> findTopByPeriodTypeOrderBySnapshotDateDesc(String periodType);
}
