create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "recommendation"(
    id  VARCHAR     CONSTRAINT recommendation_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    description    VARCHAR     NOT NULL,
    is_approved BOOLEAN DEFAULT FALSE,
    creation_datetime timestamp  with time zone NOT NULL
);