ALTER TABLE "user"
ADD COLUMN approved boolean default false,
ADD COLUMN phone_numbers VARCHAR,
ADD COLUMN firstLogin boolean default true;