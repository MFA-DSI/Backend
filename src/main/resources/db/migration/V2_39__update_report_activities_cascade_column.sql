-- 1. Supprimer la contrainte existante
ALTER TABLE activity DROP CONSTRAINT fk_report_request_activity;

-- 2. Ajouter une nouvelle contrainte avec ON DELETE SET NULL
ALTER TABLE activity
ADD CONSTRAINT fk_report_request_activity FOREIGN KEY (report_request_id)
    REFERENCES report_request (id) ON DELETE SET NULL;
