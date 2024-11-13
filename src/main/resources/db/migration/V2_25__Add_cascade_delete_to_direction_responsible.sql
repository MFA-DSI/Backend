ALTER TABLE direction_responsible
DROP CONSTRAINT IF EXISTS fk_directions_responsible_id;

ALTER TABLE direction_responsible
DROP CONSTRAINT IF EXISTS fk_directions_direction_id;

-- Recréer les clés étrangères avec ON DELETE CASCADE
ALTER TABLE direction_responsible
ADD CONSTRAINT fk_directions_responsible_id
FOREIGN KEY (responsible_id)
REFERENCES "user"(id)
ON DELETE CASCADE;

ALTER TABLE direction_responsible
ADD CONSTRAINT fk_directions_direction_id
FOREIGN KEY (direction_id)
REFERENCES direction(id)
ON DELETE CASCADE;