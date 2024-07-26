CREATE TABLE IF NOT EXISTS public.job_data
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    slug        VARCHAR(255),
    company_name VARCHAR(500),
    title       VARCHAR(255),
    description TEXT,
    remote      BOOLEAN,
    url         VARCHAR(255),
    tags        VARCHAR(255),
    job_types    VARCHAR(255),
    location    VARCHAR(255),
    created_at   INT
);