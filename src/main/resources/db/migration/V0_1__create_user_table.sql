do
$$
    begin
        if not exists(select from pg_type where typname = 'role') then
            create type "role" as enum ('ADMIN', 'USER');
        end if;
    end
$$;


create extension if not exists "uuid-ossp";



CREATE TABLE IF NOT EXISTS "user"(
    id  VARCHAR     CONSTRAINT user_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    username    VARCHAR     NOT NULL,
    first_name    VARCHAR     NOT NULL,
    last_name    VARCHAR     NOT NULL,
    email    VARCHAR     NOT NULL    CONSTRAINT user_email_unique   UNIQUE,
    password    VARCHAR     NOT NULL,
    role role NOT NULL
);

