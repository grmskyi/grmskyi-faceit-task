package com.example.faceit_task.services.impls;

import com.example.faceit_task.dtos.DataDTO;
import com.example.faceit_task.dtos.DataFromPageDTO;
import com.example.faceit_task.mappers.JobMapper;
import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import com.example.faceit_task.repositories.JobRepository;
import com.example.faceit_task.utils.HttpClientForArbeitnowAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobMapper jobMapper;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private HttpClientForArbeitnowAPI httpClient;

    @InjectMocks
    private JobServiceImpl jobService;

    @Captor
    private ArgumentCaptor<DataDTO> dataDTOCaptor;

    @Captor
    private ArgumentCaptor<DataEntity> dataEntityCaptor;

    private DataDTO dataDTO;
    private DataEntity dataEntity;
    private DataFromPageDTO dataFromPageDTO;

    @BeforeEach
    void setUp() {
        dataDTO = DataDTO.builder()
                .slug("job-001")
                .companyName("TechCorp")
                .title("Software Engineer")
                .description("Develop and maintain software applications.")
                .remote(true)
                .url("https://techcorp.com/jobs/001")
                .tags(Collections.singletonList("Java"))
                .jobTypes(Collections.singletonList("Full-time"))
                .location("New York, USA")
                .createdAt(1622505600)
                .build();

        dataEntity = DataEntity.builder()
                .id(1L)
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

        dataFromPageDTO = DataFromPageDTO.builder()
                .data(Collections.singletonList(dataDTO))
                .build();
    }

    @Test
    void updateJob_shouldUpdateJobDataSuccessfully() {
        when(httpClient.fetchJobs(anyInt())).thenReturn(dataFromPageDTO);
        when(jobMapper.toEntity(any(DataDTO.class))).thenReturn(dataEntity);

        jobService.updateJob();

        verify(httpClient, times(5)).fetchJobs(anyInt());
        verify(jobMapper, times(5)).toEntity(dataDTOCaptor.capture());
        verify(jobRepository, times(5)).save(dataEntityCaptor.capture());

        List<DataDTO> capturedDataDTOs = dataDTOCaptor.getAllValues();
        List<DataEntity> capturedDataEntities = dataEntityCaptor.getAllValues();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            DataDTO capturedDataDTO = capturedDataDTOs.get(i - 1);
            DataEntity capturedDataEntity = capturedDataEntities.get(i - 1);

            assertEquals("job-001", capturedDataDTO.getSlug());
            assertEquals("TechCorp", capturedDataDTO.getCompanyName());

            System.out.println("Page: " + i + ", Fetched Job: " + capturedDataDTO);
            System.out.println("Mapped Entity: " + capturedDataEntity);
        });
    }

    @Test
    void getAllJobs_shouldReturnPagedJobs() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DataEntity> page = new PageImpl<>(Collections.singletonList(dataEntity));

        when(jobRepository.findAll(pageable)).thenReturn(page);

        Page<DataEntity> result = jobService.getAllJobs(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(jobRepository).findAll(pageable);

        System.out.println("Paged Jobs: " + result.getContent());
    }

    @Test
    void getTop10PopularJobs_shouldReturnTop10Jobs() {
        List<DataEntity> top10Jobs = Collections.singletonList(dataEntity);

        when(jobRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(top10Jobs);

        List<DataEntity> result = jobService.getTop10PopularJobs();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(jobRepository).findTop10ByOrderByCreatedAtDesc();

        System.out.println("Top 10 Jobs: " + result);
    }

    @Test
    void getLocationStats_shouldReturnLocationStatistics() {
        LocationStats locationStats = new LocationStats("New York, USA", 1L);
        List<LocationStats> locationStatsList = Collections.singletonList(locationStats);

        when(jobRepository.getLocationStats()).thenReturn(locationStatsList);

        List<LocationStats> result = jobService.getLocationStats();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(jobRepository).getLocationStats();

        System.out.println("Location Stats: " + result);
    }
}