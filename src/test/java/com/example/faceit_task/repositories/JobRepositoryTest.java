package com.example.faceit_task.repositories;

import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Test
    void findTop10ByOrderByCreatedAtDesc_shouldReturnTop10JobsOrderedByCreatedAtDesc() {
        DataEntity job1 = DataEntity.builder()
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

        DataEntity job2 = DataEntity.builder()
                .slug("job-002")
                .companyName("HealthInc")
                .title("Data Analyst")
                .description("Analyze healthcare data to improve patient outcomes.")
                .remote(false)
                .url("https://healthinc.com/jobs/002")
                .tags("Python")
                .jobTypes("Part-time")
                .location("San Francisco, USA")
                .createdAt(1625097600)
                .build();

        jobRepository.save(job1);
        jobRepository.save(job2);

        List<DataEntity> top10Jobs = jobRepository.findTop10ByOrderByCreatedAtDesc();

        assertNotNull(top10Jobs);
        assertEquals(2, top10Jobs.size());
        assertEquals("job-002", top10Jobs.get(0).getSlug());
        assertEquals("job-001", top10Jobs.get(1).getSlug());
    }

    @Test
    void getLocationStats_shouldReturnLocationStatistics() {
        DataEntity job1 = DataEntity.builder()
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

        DataEntity job2 = DataEntity.builder()
                .slug("job-002")
                .companyName("HealthInc")
                .title("Data Analyst")
                .description("Analyze healthcare data to improve patient outcomes.")
                .remote(false)
                .url("https://healthinc.com/jobs/002")
                .tags("Python")
                .jobTypes("Part-time")
                .location("San Francisco, USA")
                .createdAt(1625097600)
                .build();

        DataEntity job3 = DataEntity.builder()
                .slug("job-003")
                .companyName("EduWorld")
                .title("Product Manager")
                .description("Lead product development teams in the education sector.")
                .remote(true)
                .url("https://eduworld.com/jobs/003")
                .tags("Agile")
                .jobTypes("Full-time")
                .location("New York, USA")
                .createdAt(1627776000)
                .build();

        jobRepository.save(job1);
        jobRepository.save(job2);
        jobRepository.save(job3);

        List<LocationStats> locationStats = jobRepository.getLocationStats();

        assertNotNull(locationStats);
        assertEquals(2, locationStats.size());
        assertTrue(locationStats.stream().anyMatch(stat -> "New York, USA".equals(stat.getLocation()) && stat.getCount() == 2));
        assertTrue(locationStats.stream().anyMatch(stat -> "San Francisco, USA".equals(stat.getLocation()) && stat.getCount() == 1));
    }

}