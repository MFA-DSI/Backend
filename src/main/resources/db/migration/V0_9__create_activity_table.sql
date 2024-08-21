create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "activity"(
    id  VARCHAR     CONSTRAINT activity_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    prediction VARCHAR ,
    creation_datetime timestamp with time zone,
    observation VARCHAR,
    mission_id VARCHAR,
    recommendation_Id VARCHAR,
    next_task_id VARCHAR,
    performance_realization_id VARCHAR
);

create index if not exists activity_id_index on "activity" (id);