CREATE TABLE IF NOT EXISTS "user_notification_list" (
    id  VARCHAR     CONSTRAINT user_notification_list_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    user_id VARCHAR CONSTRAINT fk_user_notification_list REFERENCES "user" (id),
    notification_id VARCHAR CONSTRAINT fk_notification_list_user REFERENCES notification (id)
);