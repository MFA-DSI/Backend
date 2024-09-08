CREATE TABLE IF NOT EXISTS "activity_performance_realization" (
     id  VARCHAR     CONSTRAINT activity_performance_realization_list_pk  PRIMARY KEY     DEFAULT uuid_generate_v4(),
     activity_id VARCHAR CONSTRAINT fk_activity_performance_realizations REFERENCES activity(id),
     performance_realization_id VARCHAR CONSTRAINT fk_performance_realizations_activity_pk  REFERENCES performance_realization(id)
)


