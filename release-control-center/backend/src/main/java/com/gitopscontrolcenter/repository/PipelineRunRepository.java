package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.PipelineRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PipelineRunRepository extends JpaRepository<PipelineRun, Long> {
    List<PipelineRun> findTop15ByOrderByStartedAtDesc();
}
