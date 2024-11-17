-- Création de la table "report_request" avec UUID comme identifiant
CREATE TABLE report_request (
     id  VARCHAR     CONSTRAINT report_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
    direction_id VARCHAR NOT NULL,
    start_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expiration_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    comment VARCHAR(500),
    CONSTRAINT fk_direction_requesting FOREIGN KEY (direction_id)
        REFERENCES direction (id) ON DELETE CASCADE
);

-- Création de la table "custom_report_sub_directions" avec UUID comme identifiants
CREATE TABLE custom_report_sub_directions (
    report_request_id VARCHAR NOT NULL,
    sub_direction_id VARCHAR NOT NULL,
    PRIMARY KEY (report_request_id, sub_direction_id),
    CONSTRAINT fk_report_request FOREIGN KEY (report_request_id)
        REFERENCES report_request (id) ON DELETE CASCADE,
    CONSTRAINT fk_sub_direction FOREIGN KEY (sub_direction_id)
        REFERENCES direction (id) ON DELETE CASCADE
);

-- Ajout d'une colonne "report_request_id" dans la table "activity" pour la relation avec "report_request"
ALTER TABLE activity
ADD COLUMN report_request_id VARCHAR;

-- Ajout de la contrainte de clé étrangère pour l'association entre "activity" et "report_request"
ALTER TABLE activity
ADD CONSTRAINT fk_report_request_activity FOREIGN KEY (report_request_id)
    REFERENCES report_request (id) ON DELETE CASCADE;
