ALTER TABLE notification
DROP CONSTRAINT notification_report_request_id_fkey;



ALTER TABLE notification
ADD CONSTRAINT notification_report_request_id_fkey
FOREIGN KEY (report_request_id)
REFERENCES report_request(id)
ON DELETE CASCADE;
