package com.example.faceit_task.controllers;


import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import com.example.faceit_task.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    /**
     * GET /getAllJobs : Get all jobs with pagination.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in the body
     */
    @GetMapping("/getAllJobs")
    public ResponseEntity<Page<DataEntity>> getAllJobs(Pageable pageable) {
        return ResponseEntity.ok(jobService.getAllJobs(pageable));
    }

    /**
     * GET /top10 : Get the top 10 most popular jobs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of top 10 popular jobs in the body
     */
    @GetMapping("/top10")
    public ResponseEntity<List<DataEntity>> getTop10PopularJobs() {
        return ResponseEntity.ok(jobService.getTop10PopularJobs());
    }

    /**
     * GET /location-stats : Get job statistics grouped by location.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of location statistics in the body
     */
    @GetMapping("/location-stats")
    public ResponseEntity<List<LocationStats>> getLocationStats() {
        return ResponseEntity.ok(jobService.getLocationStats());
    }
}