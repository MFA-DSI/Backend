-- Ajout de la colonne parent_id dans la table direction pour établir la relation hiérarchique
ALTER TABLE direction
    ADD COLUMN IF NOT EXISTS parent_id VARCHAR,
    ADD CONSTRAINT fk_direction_parent
    FOREIGN KEY (parent_id)
    REFERENCES direction (id) ON DELETE SET NULL;

-- Création d'un index pour optimiser les requêtes sur la relation hiérarchique
CREATE INDEX IF NOT EXISTS idx_direction_parent_id ON direction(parent_id);
