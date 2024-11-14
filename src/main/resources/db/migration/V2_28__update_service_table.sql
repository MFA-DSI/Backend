ALTER TABLE service
    ADD COLUMN IF NOT EXISTS acronym VARCHAR(50);

-- Création d'un index unique pour garantir l'unicité du sigle dans chaque direction
CREATE UNIQUE INDEX IF NOT EXISTS idx_service_acronym ON direction(acronym);