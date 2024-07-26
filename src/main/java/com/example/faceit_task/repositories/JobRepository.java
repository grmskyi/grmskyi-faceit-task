package com.example.faceit_task.repositories;

import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobRepository extends JpaRepository<DataEntity, Long> {
    List<DataEntity> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT new com.example.faceit_task.pojos.LocationStats(j.location, COUNT(j)) FROM DataEntity j GROUP BY j.location")
    List<LocationStats> getLocationStats();
}