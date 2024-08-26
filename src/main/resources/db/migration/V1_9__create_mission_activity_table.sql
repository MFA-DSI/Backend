 CREATE TABLE IF NOT EXISTS "mission_activity" (
     id  VARCHAR     CONSTRAINT mission_activity_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
     mission_id VARCHAR ,
     activity_id VARCHAR
 );


ALTER TABLE mission_activity
ADD CONSTRAINT fk_mission_activity
FOREIGN KEY (mission_id)
REFERENCES mission(id);

ALTER TABLE mission_activity
ADD CONSTRAINT fk_activity_missions
FOREIGN KEY (activity_id)
REFERENCES activity(id)