package com.debesh.snaphire.application_ms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime appliedAt = LocalDateTime.now();
    private String status = "Pending";

    // --- MICROSERVICE CHANGES ---

    // Monolith: private Job job;
    // Microservice: Just the ID
    private Long jobId;

    // Monolith: private User candidate;
    // Microservice: Just the ID
    private Long candidateId;

    public Application() {
    }

    public Application(Long id, LocalDateTime appliedAt, String status, Long jobId, Long candidateId) {
        this.id = id;
        this.appliedAt = appliedAt;
        this.status = status;
        this.jobId = jobId;
        this.candidateId = candidateId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", appliedAt=" + appliedAt +
                ", status='" + status + '\'' +
                ", jobId=" + jobId +
                ", candidateId=" + candidateId +
                '}';
    }
}
