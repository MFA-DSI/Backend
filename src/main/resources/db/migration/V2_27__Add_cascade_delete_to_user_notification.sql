-- Migration pour ajouter ON DELETE CASCADE à la contrainte de clé étrangère de notification

-- Supprimer la contrainte existante sans cascade si elle existe
ALTER TABLE notification
    DROP CONSTRAINT IF EXISTS notification_new_user_id_fkey;

-- Ajouter la nouvelle contrainte avec ON DELETE CASCADE
ALTER TABLE notification
    ADD CONSTRAINT fk_notification_new_user_id
        FOREIGN KEY (new_user_id)
        REFERENCES "user"(id)
        ON DELETE CASCADE;
