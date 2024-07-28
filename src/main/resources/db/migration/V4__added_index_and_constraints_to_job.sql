CREATE INDEX idx_job_on_company_id ON job (company_id);

ALTER TABLE job
    ALTER COLUMN description SET NOT NULL;

ALTER TABLE job
    ALTER COLUMN title SET NOT NULL;