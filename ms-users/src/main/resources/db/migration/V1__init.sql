-- Create table for Address
CREATE TABLE address (
                         id SERIAL PRIMARY KEY,
                         cep VARCHAR(10) NOT NULL,
                         street VARCHAR(255) NOT NULL,
                         number VARCHAR(20) NOT NULL,
                         complement VARCHAR(255),
                         neighborhood VARCHAR(255) NOT NULL,
                         city VARCHAR(255) NOT NULL,
                         state VARCHAR(255) NOT NULL
);

-- Create enum for Role
CREATE TYPE role_type AS ENUM ('OWNER', 'CLIENT');

-- Create table for User
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       login VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       last_modified_date TIMESTAMP,
                       status BOOLEAN NOT NULL DEFAULT true,
                       role role_type NOT NULL
);

-- Relation table between User and Address (One-to-Many)
CREATE TABLE user_addresses (
                                user_id INTEGER NOT NULL,
                                address_id INTEGER NOT NULL,
                                PRIMARY KEY (user_id, address_id),
                                CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
                                CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address (id)
);
