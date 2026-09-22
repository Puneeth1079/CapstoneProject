package com.gitopscontrolcenter.repository;

import com.gitopscontrolcenter.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    Optional<ApplicationEntity> findByName(String name);
    long countByHealthStatus(String healthStatus);
}
