package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.ReleaseExperiment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReleaseExperimentRepository extends JpaRepository<ReleaseExperiment, Long> {
    Optional<ReleaseExperiment> findTopByActiveTrueOrderByStartedAtDesc();
}
