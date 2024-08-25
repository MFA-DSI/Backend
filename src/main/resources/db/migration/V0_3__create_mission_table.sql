create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "mission"(
    id  VARCHAR     CONSTRAINT mission_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    description    VARCHAR     NOT NULL
);
create index if not exists mission_id_index on "mission" (id);