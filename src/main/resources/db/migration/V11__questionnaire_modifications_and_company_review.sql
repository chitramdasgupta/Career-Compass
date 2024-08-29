CREATE TABLE company_review
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    company_id INTEGER,
    user_id    INTEGER,
    rating     INTEGER                                  NOT NULL,
    CONSTRAINT pk_companyreview PRIMARY KEY (id)
);
