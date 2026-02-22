package com.debesh.snaphire.application_ms.controller;


import com.debesh.snaphire.application_ms.entity.Application;
import com.debesh.snaphire.application_ms.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    // POST: /applications/apply?userId=1&jobId=5
    // RBAC: ONLY CANDIDATES CAN APPLY
    @PostMapping("/apply")
    public ResponseEntity<String> applyForJob(
            @RequestParam("jobId") Long jobId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (!"CANDIDATE".equals(role)) {
            return new ResponseEntity<>("Access Denied: Only Candidates can apply for jobs.", HttpStatus.FORBIDDEN);
        }
        applicationService.applyForJob(userId, jobId);
        return new ResponseEntity<>("Application created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(@PathVariable("jobId") Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
    }

    // GET: /applications/1 (Get details of a single application)
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    // DELETE: /applications/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable("id") Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok("Application withdrawn/deleted successfully");
    }

    // RBAC: ONLY RECRUITERS CAN UPDATE STATUS (e.g., Accept/Reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"RECRUITER".equals(role)) {
            return new ResponseEntity<>("Access Denied: Only Recruiters can update application status.", HttpStatus.FORBIDDEN);
        }

        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok("Status updated to " + status);
    }
}
