ALTER TABLE recommendation
ADD COLUMN activity_id VARCHAR;

ALTER TABLE recommendation
ADD CONSTRAINT fk_recommendation_activity
FOREIGN KEY (activity_id)
REFERENCES activity(id);