ALTER TABLE service
    DROP CONSTRAINT IF EXISTS service_mission_id_fkey,
    DROP CONSTRAINT IF EXISTS service_direction_id_fkey;

-- Ajouter les nouvelles contraintes avec ON DELETE CASCADE
ALTER TABLE service
    ADD CONSTRAINT fk_service_mission_id
        FOREIGN KEY (mission_id)
        REFERENCES mission(id)
        ON DELETE CASCADE;

ALTER TABLE service
    ADD CONSTRAINT fk_service_direction_id
        FOREIGN KEY (direction_id)
        REFERENCES direction(id)
        ON DELETE CASCADE;