package com.example.faceit_task.controllers;

import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import com.example.faceit_task.repositories.JobRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JobControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobRepository jobRepository;

    private DataEntity dataEntity;

    @BeforeEach
    void setup() {
        dataEntity = DataEntity.builder()
                .slug("job-001")
                .companyName("TechCorp")
                .title("Software Engineer")
                .description("Develop and maintain software applications.")
                .remote(true)
                .url("https://techcorp.com/jobs/001")
                .tags("Java")
                .jobTypes("Full-time")
                .location("New York, USA")
                .createdAt(1622505600)
                .build();
    }

    @Test
    @SneakyThrows
    void testGetAllJobs() {
        Page<DataEntity> page = new PageImpl<>(Collections.singletonList(dataEntity));

        when(jobRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/getAllJobs").param("page", "0").param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].slug").value("job-001"))
                .andExpect(jsonPath("$.content[0].companyName").value("TechCorp"));
    }

    @Test
    @SneakyThrows
    void testGetTop10PopularJobs() {
        List<DataEntity> top10Jobs = Collections.singletonList(dataEntity);

        when(jobRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(top10Jobs);

        mockMvc.perform(get("/api/v1/top10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].slug").value("job-001"))
                .andExpect(jsonPath("$[0].companyName").value("TechCorp"));
    }

    @Test
    @SneakyThrows
    void testGetLocationStats() {
        LocationStats locationStats = new LocationStats("New York, USA", 1L);
        List<LocationStats> locationStatsList = Collections.singletonList(locationStats);

        when(jobRepository.getLocationStats()).thenReturn(locationStatsList);

        mockMvc.perform(get("/api/v1/location-stats"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].location").value("New York, USA"))
                .andExpect(jsonPath("$[0].count").value(1));
    }
}