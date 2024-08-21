create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "task"(
    id  VARCHAR     CONSTRAINT task_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    description    VARCHAR     NOT NULL,
    creation_datetime timestamp  with time zone NOT NULL
);