CREATE TABLE IF NOT EXISTS "direction_mission" (
       id  VARCHAR     CONSTRAINT direction_mission_list_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
       direction_id VARCHAR CONSTRAINT fk_direction_missions REFERENCES direction(id),
       mission_id VARCHAR CONSTRAINT fk_missions_direction REFERENCES mission(id)
)