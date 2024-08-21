ALTER TABLE next_task
ADD COLUMN activity_id VARCHAR;


ALTER TABLE next_task
ADD CONSTRAINT fk_next_task_activity
FOREIGN KEY (activity_id)
REFERENCES activity(id);