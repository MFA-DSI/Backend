ALTER TABLE direction
    ADD COLUMN IF NOT EXISTS acronym VARCHAR(50);

-- Création d'un index unique pour garantir l'unicité du sigle dans chaque direction
CREATE UNIQUE INDEX IF NOT EXISTS idx_direction_acronym ON direction(acronym);