ALTER TABLE mission
ADD COLUMN direction_id VARCHAR(36),
ADD COLUMN user_id VARCHAR(36);


ALTER TABLE mission
ADD CONSTRAINT fk_missions_directions
FOREIGN KEY (direction_id)
REFERENCES direction(id);


ALTER TABLE mission
ADD CONSTRAINT fk_missions_user
FOREIGN KEY (user_id)
REFERENCES "user"(id);