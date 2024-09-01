CREATE TYPE qualification_degree_enum AS ENUM ('HIGH_SCHOOL', 'DIPLOMA', 'BACHELORS', 'MASTERS', 'PHD', 'POST_DOCTORATE');

ALTER TABLE users
    ADD COLUMN first_name  VARCHAR(255),
    ADD COLUMN middle_name VARCHAR(255),
    ADD COLUMN last_name   VARCHAR(255),
    ADD COLUMN department  VARCHAR(255),
    ADD COLUMN degree      qualification_degree_enum;