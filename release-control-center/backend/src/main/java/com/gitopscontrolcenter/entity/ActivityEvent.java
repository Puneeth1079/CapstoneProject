package com.gitopscontrolcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "activity_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "application_name")
    private String applicationName;

    @Column(nullable = false)
    private String severity;

    private ZonedDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) timestamp = ZonedDateTime.now();
        if (severity == null) severity = "INFO";
    }
}
