package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.RollbackEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RollbackEventRepository extends JpaRepository<RollbackEvent, Long> {
    List<RollbackEvent> findByApplicationNameOrderByDetectedAtDesc(String applicationName);
    List<RollbackEvent> findTop10ByOrderByDetectedAtDesc();
}
