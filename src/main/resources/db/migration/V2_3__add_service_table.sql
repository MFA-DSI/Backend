create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "service"(
    id  VARCHAR     CONSTRAINT mission_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    name    VARCHAR     NOT NULL,
    mission_id VARCHAR FOREIGN KEY mission_service_pk REFERENCES mission(id),
    direction_id VARCHAR FOREIGN KEY direction_service_pk REFERENCES direction(id)
);
create index if not exists service_id_index on "service" (id);