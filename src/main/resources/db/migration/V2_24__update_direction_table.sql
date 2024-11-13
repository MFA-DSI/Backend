-- V1_3__Add_cascade_delete_to_directions.sql

-- Supprimer les anciennes clés étrangères si elles n'ont pas l'option CASCADE
ALTER TABLE "user"
DROP CONSTRAINT IF EXISTS fk_users_directions;

ALTER TABLE mission
DROP CONSTRAINT IF EXISTS fk_missions_directions;

-- Recréer les clés étrangères avec ON DELETE CASCADE
ALTER TABLE "user"
ADD CONSTRAINT fk_users_directions
FOREIGN KEY (direction_id)
REFERENCES direction(id)
ON DELETE CASCADE;

ALTER TABLE mission
ADD CONSTRAINT fk_missions_directions
FOREIGN KEY (direction_id)
REFERENCES direction(id)
ON DELETE CASCADE;
