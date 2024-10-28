ALTER TABLE "user"
ADD CONSTRAINT unique_email UNIQUE (email);

ALTER TABLE "user"
ADD CONSTRAINT unique_phone_numbers UNIQUE (phone_numbers);
