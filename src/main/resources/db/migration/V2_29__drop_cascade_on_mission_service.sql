ALTER TABLE service
    DROP CONSTRAINT IF EXISTS fk_service_mission_id;


-- Ajouter les nouvelles contraintes avec ON DELETE CASCADE
ALTER TABLE service
    ADD CONSTRAINT fk_service_mission_id
        FOREIGN KEY (mission_id)
        REFERENCES mission(id);
