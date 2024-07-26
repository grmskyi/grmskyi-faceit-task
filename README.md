# grmskyi-faceit-task

This project is designed to fetch job data from the Arbeitnow API, save it to an H2 in-memory database, and provide a
RESTful API to access and analyze the job data. The project includes features such as pagination, sorting, fetching the
top 10 most popular jobs, and job statistics grouped by location. The application is built using Spring Boot, with
scheduled tasks to periodically update the job data.

## Features

1. **Fetch Job Data from API**: Download job data from the Arbeitnow API and save it to an H2 in-memory database.
2. **RESTful API**:
    - Get all jobs with pagination and sorting.
    - Get the top 10 most popular jobs.
    - Get job statistics grouped by location.
3. **Scheduled Tasks**: Periodically fetch and update job data.
4. **Integration Tests**: Comprehensive integration tests to ensure the functionality of the application.
5. **Cloud Deployment**: Ready for deployment to cloud environments using Docker.

## How to Run

### Prerequisites

- Docker
- Maven
- JDK 22

### Steps

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```
2. **Build the Docker image**:

   ```bash
   docker-compose up --build
   ```
   **DockerFile:**
   ```dockerfile
   # Stage 1: Build the application
   FROM maven:3.9.6-eclipse-temurin-22 AS builder
   WORKDIR /app
   COPY pom.xml .
   COPY src ./src
   RUN mvn clean package -DskipTests

   # Stage 2: Create the production image
   FROM eclipse-temurin:22-jdk-alpine AS prod
   WORKDIR /app
   COPY --from=builder /app/target/*.jar /app/job_service.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app/job_service.jar"]
   ```
   **Docker-compose.yml:**
   ```dockerfile
   version: '3.8'
   services:
    job_service:
        build:
        context: .
        dockerfile: Dockerfile
    ports:
        - 8080:8080
    environment:
        - SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
        - SPRING_DATASOURCE_USERNAME=sa
        - SPRING_DATASOURCE_PASSWORD=password
        - SPRING_FLYWAY_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
        - SPRING_FLYWAY_USER=sa
        - SPRING_FLYWAY_PASSWORD=password
    ```
## Endpoints
### Get All Jobs
- URL: /api/v1/getAllJobs
- Method: GET
- Query Params: page, size, sort
- Response: 200 OK

![Alt text](/screenshots_for_github/get_all_jobs.png?raw=true "Result data from Postman")

### Get Top 10 Popular Jobs
- URL: /api/v1/top10
- Method: GET
- Response: 200 OK

![Alt text](/screenshots_for_github/get_top_10_jobs.png?raw=true "Result data from Postman")

### Get Job Statistics by Location
- URL: /api/v1/location-stats
- Method: GET
- Response: 200 OK

![Alt text](/screenshots_for_github/get_locations.png?raw=true "Result data from Postman")

## Endpoints
The application uses Quartz to schedule the task of fetching job data from the Arbeitnow API every 20 minutes. This can be configured in the `QuartzConfig` class.
In the following docker logs, you can see that Quartz job successfully pulled data from the API and loaded it into the database:
```terminal
job_service-1  | 2024-07-26T14:18:59.915Z  INFO 1 --- [demo] [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler 'quartzScheduler' initialized from an externally provided properties instance.                                                                                        
job_service-1  | 2024-07-26T14:18:59.915Z  INFO 1 --- [demo] [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.2                                                                                                                                                        
job_service-1  | 2024-07-26T14:18:59.915Z  INFO 1 --- [demo] [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.2
job_service-1  | 2024-07-26T14:18:59.915Z  INFO 1 --- [demo] [           main] org.quartz.core.QuartzScheduler          : JobFactory set to: org.springframework.scheduling.quartz.SpringBeanJobFactory@5e05dd42
job_service-1  | 2024-07-26T14:18:59.950Z  INFO 1 --- [demo] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
job_service-1  | 2024-07-26T14:18:59.951Z  INFO 1 --- [demo] [           main] o.s.s.quartz.SchedulerFactoryBean        : Starting Quartz Scheduler now
job_service-1  | 2024-07-26T14:18:59.951Z  INFO 1 --- [demo] [           main] org.quartz.core.QuartzScheduler          : Scheduler quartzScheduler_$_NON_CLUSTERED started.
job_service-1  | 2024-07-26T14:18:59.966Z  INFO 1 --- [demo] [           main] c.e.faceit_task.FaceItTaskApplication    : Started FaceItTaskApplication in 3.101 seconds (process running for 3.417)
job_service-1  | 2024-07-26T14:19:11.420Z  INFO 1 --- [demo] [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
job_service-1  | 2024-07-26T14:19:11.420Z  INFO 1 --- [demo] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
job_service-1  | 2024-07-26T14:19:11.421Z  INFO 1 --- [demo] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
job_service-1  | 2024-07-26T14:19:11.511Z  WARN 1 --- [demo] [nio-8080-exec-1] PageModule$PlainPageSerializationWarning : Serializing PageImpl instances as-is is not supported, meaning that there is no guarantee about the stability of the resulting JSON structure!
job_service-1  |        For a stable JSON structure, please use Spring Data's PagedModel (globally via @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO))
job_service-1  |        or Spring HATEOAS and Spring Data's PagedResourcesAssembler as documented in https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.pageables.
job_service-1  |


job_service-1  | 2024-07-26T14:20:00.003Z  INFO 1 --- [demo] [eduler_Worker-1] c.e.faceit_task.quartz.ParseJobsData     : Executing ParseJobsData...
job_service-1  | 2024-07-26T14:20:01.500Z  INFO 1 --- [demo] [eduler_Worker-1] c.e.faceit_task.quartz.ParseJobsData     : ParseJobsData completed successfully.
```
## Running Tests
The project includes integration tests for controllers, services, and repositories. You can run the tests using Maven:
   ```bash
   mvn test
   ```
The result of all tests:
```terminal
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.138 s -- in com.example.faceit_task.services.impls.JobServiceImplTest
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.292 s
[INFO] Finished at: 2024-07-26T17:39:40+03:00
[INFO] ------------------------------------------------------------------------
```

## Data from database
Below is a successful data storage after Job execution:
![Alt text](/screenshots_for_github/data_from_h2.png?raw=true "data from h2")


## License
This project is licensed under the MIT License.

Feel free to modify the README.md file as per your specific requirements and add any additional information you deem necessary.
