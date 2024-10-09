ALTER TABLE notification
DROP COLUMN status;

ALTER TABLE notification
ADD COLUMN notification_type VARCHAR;