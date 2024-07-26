package com.example.faceit_task.utils;


import com.example.faceit_task.dtos.DataFromPageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpClientForArbeitnowAPI {

    @Value("${arbeitnow.base.url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();


    /**
     * Fetches job data from the Arbeitnow API for a specific page number.
     *
     * @param pageNumber the page number to fetch
     * @return the job data from the specified page, or null if an error occurs
     */
    public DataFromPageDTO fetchJobs(int pageNumber) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + pageNumber))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), DataFromPageDTO.class);
        } catch (IOException e) {
            log.error("IO error fetching jobs", e);
            return null;
        } catch (InterruptedException e) {
            log.error("Interrupted while fetching jobs", e);
            Thread.currentThread().interrupt();
            return null;
        }
    }
}