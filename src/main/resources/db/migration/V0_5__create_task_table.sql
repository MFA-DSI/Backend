create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "task"(
    id  VARCHAR     CONSTRAINT task_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    description    VARCHAR     NOT NULL,
    due_datetime  timestamp  with time zone NOT NULL
);
create index if not exists task_id_index on "task" (id);