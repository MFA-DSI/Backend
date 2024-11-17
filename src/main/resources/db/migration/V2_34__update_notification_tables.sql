ALTER TABLE notification
ADD COLUMN report_request_id VARCHAR REFERENCES report_request(id);