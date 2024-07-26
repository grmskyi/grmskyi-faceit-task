package com.example.faceit_task.services;

import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    void updateJob();

    Page<DataEntity> getAllJobs(Pageable pageable);

    List<DataEntity> getTop10PopularJobs();

    List<LocationStats> getLocationStats();
}
