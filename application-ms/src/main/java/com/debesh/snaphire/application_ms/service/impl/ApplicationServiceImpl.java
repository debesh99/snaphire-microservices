package com.debesh.snaphire.application_ms.service.impl;

import com.debesh.snaphire.application_ms.Exception.ApplicationNotFoundException;
import com.debesh.snaphire.application_ms.client.JobClient;
import com.debesh.snaphire.application_ms.client.UserClient;
import com.debesh.snaphire.application_ms.dto.JobDto;
import com.debesh.snaphire.application_ms.dto.UserDto;
import com.debesh.snaphire.application_ms.entity.Application;
import com.debesh.snaphire.application_ms.repository.ApplicationRepository;
import com.debesh.snaphire.application_ms.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobClient jobClient; // Feign Client to talk to JOB-SERVICE

    @Autowired
    private UserClient userClient; // Feign Client to talk to USER-MS

    @Override
    public void applyForJob(Long userId, Long jobId) {
        // 1. Check if the user has already applied for this job (prevent duplicates)
        if (applicationRepository.existsByJobIdAndCandidateId(jobId, userId)) {
            LOGGER.error("Application failed. User {} already applied for Job {}", userId, jobId);
            throw new RuntimeException("You have already applied for this job!");
        }

        // 2. EXTERNAL CALL: Check if User exists in User Microservice
        // If user is not found, Feign will throw an exception (usually 404)
        UserDto userDto = userClient.getUserById(userId);
        if (userDto == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // 3. EXTERNAL CALL: Check if Job exists in Job Microservice
        JobDto jobDto = jobClient.getJobById(jobId);
        if (jobDto == null) {
            throw new RuntimeException("Job not found with ID: " + jobId);
        }

        // 4. Save the Application (Store only the IDs)
        Application application = new Application();
        application.setCandidateId(userId);
        application.setJobId(jobId);
        application.setStatus("Pending"); // Default status

        applicationRepository.save(application);
        LOGGER.info("Application created successfully for User {} and Job {}", userId, jobId);
    }

    @Override
    public List<Application> getApplicationsByJobId(Long jobId) {
        // Optional: Verify job exists before fetching applications
        // JobDTO job = jobClient.getJobById(jobId);
        return applicationRepository.findByJobId(jobId);
    }

    @Override
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with ID " + id + " not found"));
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new ApplicationNotFoundException("Cannot delete. Application not found with ID: " + id);
        }
        applicationRepository.deleteById(id);
        LOGGER.info("Application ID {} deleted successfully", id);
    }

    @Override
    public void updateApplicationStatus(Long id, String status) {
        Application app = getApplicationById(id); // Re-use getApplicationById to handle exception
        app.setStatus(status);
        applicationRepository.save(app);
        LOGGER.info("Application ID {} status updated to {}", id, status);
    }
}
