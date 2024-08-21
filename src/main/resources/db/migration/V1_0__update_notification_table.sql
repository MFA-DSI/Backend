ALTER TABLE notification
ADD COLUMN activity_id VARCHAR;

ALTER TABLE notification
ADD CONSTRAINT fk_user_notification
FOREIGN KEY (activity_id)
REFERENCES "user"(id)
ON DELETE CASCADE;