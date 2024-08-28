ALTER TABLE performance_realization
ADD CONSTRAINT pk_activity_performance
FOREIGN KEY (activity_id)
REFERENCES activity(id)
ON DELETE CASCADE;