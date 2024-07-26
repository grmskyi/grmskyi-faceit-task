package com.example.faceit_task.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_data")
public class DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slug;

    @Column(name = "company_name", length = 500)
    private String companyName;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean remote;

    private String url;

    private String tags;

    @Column(name = "job_types")
    private String jobTypes;

    private String location;

    @Column(name = "created_at")
    private Integer createdAt;
}