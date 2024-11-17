-- Ajout de la colonne 'responsible_id' à la table 'custom_report_request'
ALTER TABLE report_request
ADD COLUMN responsible_id VARCHAR  NOT NULL;

-- Création de la clé étrangère entre 'responsible_id' et la table 'user'
ALTER TABLE report_request
ADD CONSTRAINT fk_responsible_user_request
FOREIGN KEY (responsible_id) REFERENCES "user"(id);
