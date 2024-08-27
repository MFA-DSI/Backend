ALTER TABLE notification
ADD COLUMN activity_id VARCHAR;

ALTER TABLE notification
ADD CONSTRAINT fk_user_activity_notification
FOREIGN KEY (activity_id)
REFERENCES "activity"(id)
ON DELETE CASCADE;

ALTER TABLE notification
ADD CONSTRAINT fk_user_activity_user_notification
FOREIGN KEY (user_id)
REFERENCES "user"(id)
ON DELETE CASCADE