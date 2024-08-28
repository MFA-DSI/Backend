ALTER TABLE task
ADD COLUMN activity_id VARCHAR;

ALTER TABLE task
ADD CONSTRAINT fk_task_activity
FOREIGN KEY (activity_id)
REFERENCES activity(id);