-- Migration V5: Add ON DELETE CASCADE to address.user_id foreign key

-- Step 1: Drop old FK constraint
ALTER TABLE address
DROP
CONSTRAINT IF EXISTS fk_address_user;

-- Step 2: Recreate it with ON DELETE CASCADE
ALTER TABLE address
    ADD CONSTRAINT fk_address_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
