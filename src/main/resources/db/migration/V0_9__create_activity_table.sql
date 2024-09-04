create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "activity"(
    id  VARCHAR     CONSTRAINT activity_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    prediction VARCHAR ,
    description VARCHAR,
    creation_datetime timestamp with time zone,
    observation VARCHAR,
    mission_id VARCHAR
);

create index if not exists activity_id_index on "activity" (id);
create index if not exists creation_datetime_id_index on "activity" (creation_datetime);