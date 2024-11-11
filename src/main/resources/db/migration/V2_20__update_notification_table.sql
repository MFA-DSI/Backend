ALTER TABLE notification
ADD COLUMN new_user_id VARCHAR REFERENCES "user"(id);
