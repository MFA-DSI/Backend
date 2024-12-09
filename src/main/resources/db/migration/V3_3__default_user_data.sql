BEGIN;
INSERT INTO "user" (
  id, first_name, last_name, email, password, role, direction_id, grade, function, approved, phone_numbers, first_login, staff
)
VALUES (
  '6b9b7b55-79e2-43ac-9858-1d15abab68da',
  'admin',
  'DSI',
  'admin4@dsi.com',
  '$2a$10$uUXdT2PaYy.GXkIJ4w6lhe2wV/c8brKv.6PfvtleRJ/jBmZ3YRYX6',
  'ADMIN',
  '550e8400-e29b-41d4-a716-446655440032',
  'PC',
  'DÚveloppeur',
  't',
  NULL,
  'f',
  'true'
);
COMMIT;
ROLLBACK;
