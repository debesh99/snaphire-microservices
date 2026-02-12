package com.debesh.snaphire.application_ms.client;

import com.debesh.snaphire.application_ms.dto.JobDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-MS")
public interface JobClient {
    @GetMapping("/jobs/{id}")
    JobDto getJobById(@PathVariable("id") Long id);
}
