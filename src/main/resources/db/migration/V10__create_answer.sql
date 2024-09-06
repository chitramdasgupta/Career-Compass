CREATE TABLE answer
(
    id                 INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id            INTEGER                                  NOT NULL,
    job_application_id INTEGER                                  NOT NULL,
    question_id        INTEGER                                  NOT NULL,
    response           TEXT,
    CONSTRAINT pk_answer PRIMARY KEY (id)
);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_JOBAPPLICATION FOREIGN KEY (job_application_id) REFERENCES job_application (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);