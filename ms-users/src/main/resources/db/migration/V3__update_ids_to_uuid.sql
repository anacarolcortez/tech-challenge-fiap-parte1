-- Migration V3: Update the id columns to UUID in the users and address tables

-- Ensure the pgcrypto extension is available for UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Add a new UUID column to users table and update it with UUID values
ALTER TABLE users ADD COLUMN id_temp UUID DEFAULT gen_random_uuid();

-- Add a new UUID column to address table and update it with UUID values
ALTER TABLE address ADD COLUMN id_temp UUID DEFAULT gen_random_uuid();

-- Add a new UUID column to the user_addresses table (for foreign keys)
ALTER TABLE user_addresses ADD COLUMN user_id_temp UUID;
ALTER TABLE user_addresses ADD COLUMN address_id_temp UUID;

-- Drop the foreign key constraint before dropping the old columns
ALTER TABLE user_addresses DROP CONSTRAINT fk_user;
ALTER TABLE user_addresses DROP CONSTRAINT fk_address;

-- Copy data from the old integer columns to the new UUID columns
UPDATE users SET id_temp = gen_random_uuid();
UPDATE address SET id_temp = gen_random_uuid();
UPDATE user_addresses SET user_id_temp = gen_random_uuid();
UPDATE user_addresses SET address_id_temp = gen_random_uuid();

-- Drop the old integer id columns
ALTER TABLE users DROP COLUMN id;
ALTER TABLE address DROP COLUMN id;
ALTER TABLE user_addresses DROP COLUMN user_id;
ALTER TABLE user_addresses DROP COLUMN address_id;

-- Rename the new UUID columns to 'id', 'user_id', 'address_id'
ALTER TABLE users RENAME COLUMN id_temp TO id;
ALTER TABLE address RENAME COLUMN id_temp TO id;
ALTER TABLE user_addresses RENAME COLUMN user_id_temp TO user_id;
ALTER TABLE user_addresses RENAME COLUMN address_id_temp TO address_id;

-- Add a primary key to the 'id' column in the 'users' table to ensure it can be referenced
ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-- Add a primary key to the 'id' column in the 'address' table (if not already done)
ALTER TABLE address ADD CONSTRAINT address_pkey PRIMARY KEY (id);

-- Recreate the foreign key constraints after renaming the columns
ALTER TABLE user_addresses ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_addresses ADD CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address (id);

-- Optionally, if you want to add indices on UUID columns
CREATE INDEX IF NOT EXISTS idx_users_id ON users(id);
CREATE INDEX IF NOT EXISTS idx_address_id ON address(id);
CREATE INDEX IF NOT EXISTS idx_user_addresses_user_id ON user_addresses(user_id);
CREATE INDEX IF NOT EXISTS idx_user_addresses_address_id ON user_addresses(address_id);
