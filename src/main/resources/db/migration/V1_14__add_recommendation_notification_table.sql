ALTER TABLE notification
ADD COLUMN recommendation_id VARCHAR CONSTRAINT fk_notification_recommendations   REFERENCES recommendation(id);



