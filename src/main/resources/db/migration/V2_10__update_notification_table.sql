ALTER TABLE notification
ADD COLUMN mission_id VARCHAR REFERENCES mission(id),
ADD COLUMN task_id VARCHAR REFERENCES next_task(id);
