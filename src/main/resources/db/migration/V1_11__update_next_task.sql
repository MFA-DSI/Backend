CREATE TABLE IF NOT EXISTS "next_task_activity"(
      id  VARCHAR     CONSTRAINT next_task_activity_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
      next_task_id VARCHAR,
      activity_id VARCHAR
);

ALTER TABLE next_task_activity
ADD CONSTRAINT fk_next_tasks_activity
FOREIGN KEY (next_task_id)
REFERENCES next_task(id)
ON DELETE CASCADE;


ALTER TABLE next_task_activity
ADD CONSTRAINT fk_activity_next_tasks
FOREIGN KEY (activity_id)
REFERENCES activity(id)
ON DELETE CASCADE;