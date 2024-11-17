-- Supprimer la table intermédiaire utilisée pour les sous-directions
DROP TABLE IF EXISTS custom_report_sub_directions;

-- Ajouter une nouvelle colonne pour la direction cible dans la table "report_request"
ALTER TABLE report_request
ADD COLUMN target_direction_id VARCHAR(36);

-- Mettre à jour les données existantes (si applicable)
-- Si vous souhaitez conserver une seule direction cible, vous pouvez mapper les données existantes ici.
-- Ex: Associer une première sous-direction existante comme direction cible (à adapter à votre contexte)
-- UPDATE report_request
-- SET target_direction_id = (SELECT sub_direction_id FROM custom_report_sub_directions WHERE custom_report_sub_directions.report_request_id = report_request.id LIMIT 1);

-- Ajouter une contrainte de clé étrangère pour la nouvelle colonne "target_direction_id"
ALTER TABLE report_request
ADD CONSTRAINT fk_target_direction FOREIGN KEY (target_direction_id)
REFERENCES direction (id) ON DELETE CASCADE;

-- Supprimer l'ancienne relation Many-to-Many, car elle n'est plus nécessaire
ALTER TABLE report_request
DROP COLUMN direction_id; -- Si ce champ existait déjà et est remplacé.

-- Assurez-vous que la colonne "requesting_direction_id" existe toujours pour identifier les demandeurs
ALTER TABLE report_request
ADD COLUMN requesting_direction_id VARCHAR(36) NOT NULL;

-- Ajouter une contrainte de clé étrangère pour "requesting_direction_id" si elle n'existe pas
ALTER TABLE report_request
ADD CONSTRAINT fk_requesting_direction FOREIGN KEY (requesting_direction_id)
REFERENCES direction (id) ON DELETE CASCADE;


ALTER TABLE report_request
DROP COLUMN IF EXISTS start_date;