ALTER TABLE mission
ADD COLUMN user_id VARCHAR(36);



ALTER TABLE mission
ADD CONSTRAINT fk_missions_user
FOREIGN KEY (user_id)
REFERENCES "user"(id);

ALTER TABLE mission
ADD CONSTRAINT fk_missions_activity
FOREIGN KEY (activity_id)
REFERENCES activity(id);