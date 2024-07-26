package com.example.faceit_task.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDTO {

    private String slug;

    @JsonProperty("company_name")
    private String companyName;

    private String title;

    private String description;

    private Boolean remote;

    private String url;

    private List<String> tags = new ArrayList<>();

    @JsonProperty("job_types")
    private List<String> jobTypes = new ArrayList<>();

    private String location;

    @JsonProperty("created_at")
    private Integer createdAt;
}