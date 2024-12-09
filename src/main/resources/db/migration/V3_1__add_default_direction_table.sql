BEGIN;

-- Directions principales
INSERT INTO direction (id, name, acronym,parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440089', 'Ministère des Forces Armées', 'MFA',null),
    ('550e8400-e29b-41d4-a716-446655440090', 'Ministère Délégué chargé de la Gendarmerie', 'MDG','550e8400-e29b-41d4-a716-446655440089');

-- Sous-directions de MDG

INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440002', 'Chef de l''Etat-Major des Armées', 'CEMA', '550e8400-e29b-41d4-a716-446655440089'),
    ('550e8400-e29b-41d4-a716-446655440003', 'Commandement de la Gendarmerie Nationale', 'COMGN', '550e8400-e29b-41d4-a716-446655440090');


-- Cabinet sous MFA
INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440004', 'Cabinet', 'CAB', '550e8400-e29b-41d4-a716-446655440089'),
 ('550e8400-e29b-41d4-a716-446655440005', 'Personne Responsable des Marchés Publics', 'PRMP', '550e8400-e29b-41d4-a716-446655440089'),
  ('550e8400-e29b-41d4-a716-446655440006', 'Secretariat General', 'SG', '550e8400-e29b-41d4-a716-446655440089'),
  ('550e8400-e29b-41d4-a716-446655440007', 'Unité de Gestion de Passation des Marchés', 'UGPM', '550e8400-e29b-41d4-a716-446655440005'),
('550e8400-e29b-41d4-a716-446655440011', 'Coordination Générale des Projets', 'CGP', '550e8400-e29b-41d4-a716-446655440089');



-- Sous-directions du SG 1
INSERT INTO direction (id, name, acronym, parent_id) VALUES
      ('550e8400-e29b-41d4-a716-446655440017', 'Académie Militaire', 'ACMIL', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440018', 'Régiment de Garde Présidentielle', 'RGP', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440019', 'Bataillon de Sécurité de la Primature', 'BSP', '550e8400-e29b-41d4-a716-446655440011');


-- Sous direction de SG 2
INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440020', 'Centre Hospitalier de  Soavinandriana', 'CENHOSOA', '550e8400-e29b-41d4-a716-446655440011'),
   ('550e8400-e29b-41d4-a716-446655440021', 'Office National Malagasy des Anciens Combattants et Victimes de Guerre', 'ONMAC', '550e8400-e29b-41d4-a716-446655440011'),
   ('550e8400-e29b-41d4-a716-446655440022', 'Usine Militaire de Moramanga', 'UMM', '550e8400-e29b-41d4-a716-446655440011'),
('550e8400-e29b-41d4-a716-446655440023', 'Office Militaire des Sports et de la Culture', 'OMSC', '550e8400-e29b-41d4-a716-446655440011');




-- Sous-directions de SG 3
INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440030', 'Direction Générale de la Planification Stratégiques', 'DGPS', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440031', 'Direction Générale des Organismes de Défense', 'DGOD', '550e8400-e29b-41d4-a716-446655440011');

-- Sous-directions de SG4
INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440032', 'Direction du Système d’Information', 'DSI', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440033', 'Direction de la Communication', 'DICO', '550e8400-e29b-41d4-a716-446655440011'),
 ('550e8400-e29b-41d4-a716-446655440034', 'Direction des Ressources Humaines', 'DRH', '550e8400-e29b-41d4-a716-446655440011'),
 ('550e8400-e29b-41d4-a716-446655440035', 'Direction Administratives et Financières', 'DAF', '550e8400-e29b-41d4-a716-446655440011'),
('550e8400-e29b-41d4-a716-446655440036', 'Direction des Affaires Juridiques', 'DAJ', '550e8400-e29b-41d4-a716-446655440011');


-- Sous direction DGPS
 INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440047', 'Direction de la Défense', 'DD', '550e8400-e29b-41d4-a716-446655440030'),
  ('550e8400-e29b-41d4-a716-446655440048', 'Direction des Réformes', 'DR', '550e8400-e29b-41d4-a716-446655440030'),
  ('550e8400-e29b-41d4-a716-446655440049', 'Direction de la Planification et de l’Anticipation Stratégique', 'DPAS', '550e8400-e29b-41d4-a716-446655440030');


-- Sous direction DGOD
 INSERT INTO direction (id, name, acronym, parent_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440050', 'Direction des Combattants Nationalistes', 'DCN', '550e8400-e29b-41d4-a716-446655440031'),
  ('550e8400-e29b-41d4-a716-446655440051', 'Direction du Service Nationale et de la Gestion des Réserves', 'DSNGR', '550e8400-e29b-41d4-a716-446655440031'),
  ('550e8400-e29b-41d4-a716-446655440052', 'Direction Centrale du Service de Santé Militaire', 'DCSSM', '550e8400-e29b-41d4-a716-446655440031');
COMMIT;

ROLLBACK;