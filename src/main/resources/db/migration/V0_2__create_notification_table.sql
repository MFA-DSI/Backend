create extension if not exists "uuid-ossp";

CREATE TABLE IF NOT EXISTS "notification"(
    id  VARCHAR     CONSTRAINT notification_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    description    VARCHAR     NOT NULL,
    user_id VARCHAR,
    viewStatus  BOOLEAN DEFAULT FALSE
);