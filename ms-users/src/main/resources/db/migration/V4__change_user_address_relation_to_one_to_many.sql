-- Migration V4: Change N:N to 1: N between users and address

-- Step 1: Drop the user_addresses table
DROP TABLE IF EXISTS user_addresses;

-- Step 2: Add the user_id column (UUID) to the address table
ALTER TABLE address
    ADD COLUMN user_id UUID;

-- Step 3: Add the foreign key constraint
ALTER TABLE address
    ADD CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id);

-- (Optional) Step 4: Add an index on user_id for performance
CREATE INDEX IF NOT EXISTS idx_address_user_id ON address(user_id);
