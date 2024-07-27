CREATE TABLE job
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title       VARCHAR(255),
    description VARCHAR(255),
    company_id  INTEGER                                  NOT NULL,
    CONSTRAINT pk_job PRIMARY KEY (id)
);

ALTER TABLE job
    ADD CONSTRAINT FK_JOB_ON_COMPANY FOREIGN KEY (company_id) REFERENCES company (id);