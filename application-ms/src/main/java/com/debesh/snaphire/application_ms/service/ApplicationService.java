package com.debesh.snaphire.application_ms.service;

import com.debesh.snaphire.application_ms.entity.Application;
import java.util.List;

public interface ApplicationService {
    void applyForJob(Long userId, Long jobId);

    List<Application> getApplicationsByJobId(Long jobId);

    Application getApplicationById(Long id);

    List<Application> getAllApplications();

    void deleteApplication(Long id);

    void updateApplicationStatus(Long id, String status);
}
