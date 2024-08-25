create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "next_task"(
    id  VARCHAR     CONSTRAINT next_task_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    description    VARCHAR     NOT NULL,
    due_datetime  timestamp  with time zone NOT NULL
);

create index if not exists next_task_id_index on "next_task" (id);