package com.debesh.snaphire.job_ms.service;

import com.debesh.snaphire.job_ms.entity.Job;
import com.debesh.snaphire.job_ms.exception.JobNotFoundException;

import java.util.List;

public interface JobService{
    void createJob(Job job);

    Job getJobById(Long id) throws JobNotFoundException;

    void deleteJobById(Long id) throws JobNotFoundException;

    void updateJob(Long id, Job updatedJob) throws JobNotFoundException;

    List<Job> findAllJobs();
}
