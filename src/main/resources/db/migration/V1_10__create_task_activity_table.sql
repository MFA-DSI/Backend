CREATE TABLE IF NOT EXISTS "task_activity"(
    id  VARCHAR     CONSTRAINT task_activity_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    task_id VARCHAR,
    activity_id VARCHAR
);

ALTER TABLE task_activity
ADD CONSTRAINT fk_tasks_activity
FOREIGN KEY (task_id)
REFERENCES task(id)
ON DELETE CASCADE;

ALTER TABLE task_activity
ADD CONSTRAINT fk_activity_tasks
FOREIGN KEY (activity_id)
REFERENCES activity(id)
ON DELETE CASCADE;