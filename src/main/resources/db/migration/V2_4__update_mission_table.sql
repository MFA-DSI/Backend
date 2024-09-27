ALTER TABLE mission
ADD COLUMN service_id VARCHAR REFERENCES service(id);