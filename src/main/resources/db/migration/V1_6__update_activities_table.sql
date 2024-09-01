ALTER TABLE activity
ADD CONSTRAINT fk_activity_mission
FOREIGN KEY (mission_id)
REFERENCES mission(id)
ON DELETE CASCADE;

-- Ajout des clés étrangères pour la relation OneToOne avec PerformanceRealization
ALTER TABLE activity
ADD CONSTRAINT fk_activity_performance_realization
FOREIGN KEY (performance_realization_id)
REFERENCES performance_realization(id)
ON DELETE CASCADE;

