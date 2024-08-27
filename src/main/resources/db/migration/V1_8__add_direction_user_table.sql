 CREATE TABLE IF NOT EXISTS "direction_responsible" (
     id  VARCHAR     CONSTRAINT direction_responsible_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
     responsible_id VARCHAR ,
     direction_id VARCHAR
 );

 ALTER TABLE direction_responsible
 ADD CONSTRAINT fk_directions_responsible_id
 FOREIGN KEY (responsible_id)
 REFERENCES "user"(id);

 ALTER TABLE direction_responsible
 ADD CONSTRAINT fk_directions_direction_id
 FOREIGN KEY (direction_id)
 REFERENCES direction(id);

