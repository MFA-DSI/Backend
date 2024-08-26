create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "performance_realization"(
    id  VARCHAR     CONSTRAINT pa_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    KPI    BIGINT ,
    realization VARCHAR,
    activity_id VARCHAR
);

create index if not exists kpi_id_index on "performance_realization" (id);