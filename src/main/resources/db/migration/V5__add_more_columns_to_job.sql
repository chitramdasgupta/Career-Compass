DO
$$
    BEGIN
        CREATE TYPE job_location_enum AS ENUM ('REMOTE', 'HYBRID', 'OFFICE');
    EXCEPTION
        WHEN duplicate_object THEN null; -- If it already exists, do nothing
    END
$$;

ALTER TABLE job
    ADD COLUMN IF NOT EXISTS minimum_requirement TEXT,
    ADD COLUMN IF NOT EXISTS desired_requirement TEXT,
    ADD COLUMN IF NOT EXISTS city                VARCHAR(255),
    ADD COLUMN IF NOT EXISTS country             VARCHAR(255)      NOT NULL default 'IN',
    ADD COLUMN IF NOT EXISTS job_location        job_location_enum NOT NULL DEFAULT 'OFFICE',
    ADD COLUMN IF NOT EXISTS minimum_salary      DECIMAL(8, 2),
    ADD COLUMN IF NOT EXISTS maximum_salary      DECIMAL(8, 2),
    ADD COLUMN IF NOT EXISTS currency            VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_job_on_country ON job (country);
CREATE INDEX IF NOT EXISTS idx_job_on_currency ON job (currency);

ALTER TABLE job
    ADD CONSTRAINT chk_salary_range CHECK ((minimum_salary IS NULL OR maximum_salary IS NULL OR
                                            minimum_salary <= maximum_salary));
