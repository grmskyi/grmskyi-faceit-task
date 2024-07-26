package com.example.faceit_task.services.impls;

import com.example.faceit_task.mappers.JobMapper;
import com.example.faceit_task.models.DataEntity;
import com.example.faceit_task.pojos.LocationStats;
import com.example.faceit_task.repositories.JobRepository;
import com.example.faceit_task.services.JobService;
import com.example.faceit_task.utils.HttpClientForArbeitnowAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final HttpClientForArbeitnowAPI httpClient;

    /**
     * Updates the job data by fetching it from an external source and saving it to the repository.
     */
    @Override
    public void updateJob() {
        IntStream.rangeClosed(1, 5)
                .mapToObj(httpClient::fetchJobs)
                .filter(Objects::nonNull)
                .flatMap(dataFromPageDTO -> dataFromPageDTO.getData().stream())
                .map(jobMapper::toEntity)
                .forEach(jobRepository::save);
    }

    /**
     * Retrieves all jobs with pagination.
     *
     * @param pageable the pagination information
     * @return a page of job entities
     */
    @Override
    public Page<DataEntity> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    /**
     * Retrieves the top 10 most popular jobs.
     *
     * @return a list of the top 10 most popular job entities
     */
    @Override
    public List<DataEntity> getTop10PopularJobs() {
        return jobRepository.findTop10ByOrderByCreatedAtDesc();
    }

    /**
     * Retrieves job statistics grouped by location.
     *
     * @return a list of location statistics
     */
    @Override
    public List<LocationStats> getLocationStats() {
        return jobRepository.getLocationStats();
    }
}