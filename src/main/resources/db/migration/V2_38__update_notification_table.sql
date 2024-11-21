ALTER TABLE notification
ADD COLUMN responsible VARCHAR  NOT  NULL;


ALTER TABLE report_request
ADD COLUMN started_at timestamp  with time zone NOT NULL;