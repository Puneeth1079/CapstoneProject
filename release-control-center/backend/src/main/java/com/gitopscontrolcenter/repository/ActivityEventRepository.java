package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.ActivityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityEventRepository extends JpaRepository<ActivityEvent, Long> {
    List<ActivityEvent> findTop30ByOrderByTimestampDesc();
}
