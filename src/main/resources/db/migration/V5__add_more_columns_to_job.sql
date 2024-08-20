CREATE TYPE job_location_enum AS ENUM ('REMOTE', 'HYBRID', 'OFFICE');

ALTER TABLE job
    ADD COLUMN minimum_requirement TEXT,
    ADD COLUMN desired_requirement TEXT,
    ADD COLUMN city                VARCHAR(255),
    ADD COLUMN country             VARCHAR(255)      NOT NULL default 'IN',
    ADD COLUMN job_location        job_location_enum NOT NULL DEFAULT 'OFFICE',
    ADD COLUMN minimum_salary      DECIMAL(8, 2),
    ADD COLUMN maximum_salary      DECIMAL(8, 2),
    ADD COLUMN currency            VARCHAR(255);

CREATE INDEX idx_job_on_country ON job (country);
CREATE INDEX idx_job_on_currency ON job (currency);

ALTER TABLE job
    ADD CONSTRAINT chk_salary_range CHECK ((minimum_salary IS NULL OR maximum_salary IS NULL OR
                                            minimum_salary <= maximum_salary));
