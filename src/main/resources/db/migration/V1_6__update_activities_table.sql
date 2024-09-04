ALTER TABLE activity
ADD CONSTRAINT fk_activity_mission
FOREIGN KEY (mission_id)
REFERENCES mission(id)
ON DELETE CASCADE;

