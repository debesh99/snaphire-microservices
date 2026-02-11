package com.debesh.snaphire.job_ms.repository;

import com.debesh.snaphire.job_ms.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

}
