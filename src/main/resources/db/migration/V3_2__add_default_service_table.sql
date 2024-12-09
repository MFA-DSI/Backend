BEGIN;

-- Service rattaché de SG
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440024', 'Service de la Documentation et des Archives', 'SDA', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440025', 'Service des Relations Internationales', 'SRI', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440026', 'Service des Affaires Générales et Techniques', 'SAGT', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440027', 'Service  des Matériels Roulants', 'SMR', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440028', 'Service  Administratif et Financier', 'SAF', '550e8400-e29b-41d4-a716-446655440011');


-- Sous-service de CGP
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440037', 'Service des Etudes Prospectives', 'SEP', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440038', 'Direction SSCOP', 'SSCOP', '550e8400-e29b-41d4-a716-446655440011'),
    ('550e8400-e29b-41d4-a716-446655440039', 'Direction SBGD', 'SGBD', '550e8400-e29b-41d4-a716-446655440011');


-- Sous-service RGP
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440040', 'Sécrétariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440018');

-- Sous-service BSP
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440041', 'Sécrétariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440019');

-- Sous-service ACMIL
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440042', 'Sécrétariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440017');


-- Sous-service ONMAC
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440043','Sécrétariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440021');

-- Sous service UMM
  INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440044','Sécrétariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440022');

-- Sous service DGPS
  INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440045','Sécrétariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440023');





-- Sous service DD
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440053','Service de Documentation', 'SD', '550e8400-e29b-41d4-a716-446655440047'),
 ('550e8400-e29b-41d4-a716-446655440054','Service de la Lutte contre la Piraterie', 'SLP', '550e8400-e29b-41d4-a716-446655440047'),
 ('550e8400-e29b-41d4-a716-446655440055','Service de la Défense Environnementale et Economique', 'SDEE', '550e8400-e29b-41d4-a716-446655440047'),
 ('550e8400-e29b-41d4-a716-446655440755','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440047');


-- Sous service DR
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440056', 'Service de Pilotage des Réformes du Secteur de la Sécurité', 'SPRSS', '550e8400-e29b-41d4-a716-446655440048'),
 ('550e8400-e29b-41d4-a716-446655440954','Service de Suivi d’Acquisition des Equipements et des Matériels Stratégiques', 'SSAEMS', '550e8400-e29b-41d4-a716-446655440048'),
 ('550e8400-e29b-41d4-a716-446655440955','Service de la Protection Civile', 'SPC', '550e8400-e29b-41d4-a716-446655440048'),
  ('550e8400-e29b-41d4-a716-446655440655','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440048');



-- Sous service DPAS
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655441056','Service des Etudes Prospectives', 'SEP', '550e8400-e29b-41d4-a716-446655440049'),
 ('550e8400-e29b-41d4-a716-446655442054','Service de la Planification Stratégique', 'SPS', '550e8400-e29b-41d4-a716-446655440049'),
 ('550e8400-e29b-41d4-a716-446655440855','Service des Systèmes de Forces Futures', 'SSFF', '550e8400-e29b-41d4-a716-446655440049'),
  ('550e8400-e29b-41d4-a716-446655440555','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440049');




-- Sous service  DCN
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440057','Service des Affaires Générales et Administratives', 'SAGA', '550e8400-e29b-41d4-a716-446655440050'),
 ('550e8400-e29b-41d4-a716-446655440059','Service de l''Action Sociale', 'SAS', '550e8400-e29b-41d4-a716-446655440050'),
 ('550e8400-e29b-41d4-a716-446655440058','Service d''Enquête et du Livre d''Or', 'SELOR', '550e8400-e29b-41d4-a716-446655440050'),
  ('550e8400-e29b-41d4-a716-446655440558','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440050');



-- Sous service DSNGR
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-44665544060','Service National', 'SN', '550e8400-e29b-41d4-a716-446655440051'),
 ('550e8400-e29b-41d4-a716-446655440061','Service de Recrutement et de Révision', 'SRR', '550e8400-e29b-41d4-a716-446655440051'),
 ('550e8400-e29b-41d4-a716-446655440062','Service Technique', 'ST', '550e8400-e29b-41d4-a716-446655440051'),
 ('550e8400-e29b-41d4-a716-446655440063','Service de la Mobilisation et de la Gestion des Réserves', 'SMGR', '550e8400-e29b-41d4-a716-446655440051'),
 ('550e8400-e29b-41d4-a716-446655440064','Service Administratif et Financier', 'SAF', '550e8400-e29b-41d4-a716-446655440051'),
  ('550e8400-e29b-41d4-a716-446655440564','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440051');



-- Sous service  DCSSM
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-44665544065','Service Technique', 'ST', '550e8400-e29b-41d4-a716-446655440052'),
 ('550e8400-e29b-41d4-a716-446655440066','Service Financier', 'SF', '550e8400-e29b-41d4-a716-446655440052'),
 ('550e8400-e29b-41d4-a716-446655440067','Service Administratif', 'SA', '550e8400-e29b-41d4-a716-446655440052'),
 ('550e8400-e29b-41d4-a716-446655440068','Service Central de Pharmacie et de Laboratoire', 'SPCL', '550e8400-e29b-41d4-a716-446655440052'),
 ('550e8400-e29b-41d4-a716-446655440069','Service de l''Intervention Médicale d’Urgence', 'SIMU', '550e8400-e29b-41d4-a716-446655440052'),
    ('550e8400-e29b-41d4-a716-44665544070','Service de l''Aumônerie Militaire', 'SAM', '550e8400-e29b-41d4-a716-446655440052'),
 ('550e8400-e29b-41d4-a716-446655440071','Hôpital Militaire Antsiranana', 'HOMIANTSI', '550e8400-e29b-41d4-a716-446655440052'),
    ('550e8400-e29b-41d4-a716-44665544072','Services Régionaux de Santé Militaire', 'SRSM', '550e8400-e29b-41d4-a716-446655440052'),
 ('550e8400-e29b-41d4-a716-446655440073','Centre de Réforme des Forces Armées', 'CREFA', '550e8400-e29b-41d4-a716-446655440052'),
('550e8400-e29b-41d4-a716-446655440074','Service de la Santé de la Reproduction et des Affaires Sociales', 'SSRAS', '550e8400-e29b-41d4-a716-446655440052'),
('550e8400-e29b-41d4-a716-446655440574','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440052');



-- Sous service DSI
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440075','Service Central d''Informatique', 'SCI', '550e8400-e29b-41d4-a716-446655440032'),
 ('550e8400-e29b-41d4-a716-446655440076','Service de l''Administration des Données', 'SAD', '550e8400-e29b-41d4-a716-446655440032'),
 ('550e8400-e29b-41d4-a716-446655440077','Service de la Maintenance et de la Formation', 'SMF', '550e8400-e29b-41d4-a716-446655440032'),
  ('550e8400-e29b-41d4-a716-446655440577','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440032');



-- Sous service DICO
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440176','Service Central de l''Informatique', 'SCI', '550e8400-e29b-41d4-a716-446655440033'),
 ('550e8400-e29b-41d4-a716-446655440277','Service de la Communication Externe', 'SME', '550e8400-e29b-41d4-a716-446655440033'),
 ('550e8400-e29b-41d4-a716-446655440278','Service Média', 'SM', '550e8400-e29b-41d4-a716-446655440033'),
 ('550e8400-e29b-41d4-a716-446655440279','Laboratoire de Langues', 'LL', '550e8400-e29b-41d4-a716-446655440033'),
  ('550e8400-e29b-41d4-a716-446655440579','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440033');




-- Sous service DRH
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440080','Service de l''Administration du Personnel Militaire et de la Chancellerie', 'SAPMC', '550e8400-e29b-41d4-a716-446655440035'),
 ('550e8400-e29b-41d4-a716-446655440081','Service de l''Administration du Personnel Civil', 'SAPC', '550e8400-e29b-41d4-a716-446655440035'),
 ('550e8400-e29b-41d4-a716-446655440082','Service de la  Formation et des Stages', 'SFS', '550e8400-e29b-41d4-a716-446655440035'),
 ('550e8400-e29b-41d4-a716-446655440083','Service des Effectifs', 'SEFF', '550e8400-e29b-41d4-a716-446655440035'),
  ('550e8400-e29b-41d4-a716-446655440583','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440035');



-- Sous service DAF
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440084','Service du Budget et du Contrôle Administratif', 'SBCA', '550e8400-e29b-41d4-a716-446655440034'),
 ('550e8400-e29b-41d4-a716-446655440085',' Service Central Administratif et Financier', 'SCAF', '550e8400-e29b-41d4-a716-446655440034'),
 ('550e8400-e29b-41d4-a716-446655440086','Service de la Logistique et des Domaines', 'SLD', '550e8400-e29b-41d4-a716-446655440034'),
  ('550e8400-e29b-41d4-a716-446655440586','Secretaiat', 'SIAT', '550e8400-e29b-41d4-a716-446655440034');


-- Sous service DAJ
INSERT INTO service (id, name, acronym, direction_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440087','Service des Etudes Juridiques', 'SEJ', '550e8400-e29b-41d4-a716-446655440035'),
 ('550e8400-e29b-41d4-a716-446655440088','Service du Contentieux', 'SC', '550e8400-e29b-41d4-a716-446655440035'),
 ('550e8400-e29b-41d4-a716-446655440102','Service de l''Ethique et de la Déontologie', 'SED', '550e8400-e29b-41d4-a716-446655440035'),
('550e8400-e29b-41d4-a716-446655440100','Cellule Anti-Corruption', 'CAC', '550e8400-e29b-41d4-a716-446655440035'),
('550e8400-e29b-41d4-a716-446655440600','Secretariat', 'SIAT', '550e8400-e29b-41d4-a716-446655440035');


COMMIT;
ROLLBACK;