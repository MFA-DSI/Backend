ALTER TABLE "user"
ADD COLUMN direction_id VARCHAR(36);

-- Assuming Mission table has a direction_id column
ALTER TABLE mission
ADD COLUMN direction_id VARCHAR(36);

-- Add foreign key constraints
ALTER TABLE direction
ADD CONSTRAINT fk_directions_users
FOREIGN KEY (responsible_id)
REFERENCES "user"(id);

ALTER TABLE direction
ADD CONSTRAINT fk_directions_missions
FOREIGN KEY (mission_id)
REFERENCES mission(id);

ALTER TABLE "user"
ADD CONSTRAINT fk_users_directions
FOREIGN KEY (direction_id)
REFERENCES direction(id);

ALTER TABLE mission
ADD CONSTRAINT fk_missions_directions
FOREIGN KEY (direction_id)
REFERENCES direction(id);