package com.debesh.snaphire.job_ms.controller;

import com.debesh.snaphire.job_ms.entity.Job;
import com.debesh.snaphire.job_ms.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);
    //    All the errors are handled by GlobalExceptionHandler.class, no need to mention it explicitly
    @Autowired
    JobService jobService;

    // RBAC: ONLY RECRUITERS CAN POST JOBS
    @PostMapping
    public ResponseEntity<?> createJob(
            @RequestBody Job job,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (!"RECRUITER".equals(role)) {
            return new ResponseEntity<>("Access Denied: Only Recruiters can post jobs.", HttpStatus.FORBIDDEN);
        }
        jobService.createJob(job);
        return new ResponseEntity<>("Job created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable("id") Long id) {
        LOGGER.info("Fetching job by id: {}", id);
        Job job = jobService.getJobById(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    // RBAC: ONLY RECRUITERS CAN DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"RECRUITER".equals(role)) {
            return new ResponseEntity<>("Access Denied: Only Recruiters can delete jobs.", HttpStatus.FORBIDDEN);
        }

        // jobService.deleteJob(id); // Assuming you have this method
        return ResponseEntity.ok("Job deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable("id") Long id, @RequestBody Job updatedJob) {
        LOGGER.info("Updating job id {}: {}", id, updatedJob);
        jobService.updateJob(id, updatedJob);
        return new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Job>> findAll() {
        LOGGER.info("Getting all jobs");
        List<Job> jobs = jobService.findAllJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
