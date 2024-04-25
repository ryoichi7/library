--liquibase formatted sql

--changeset ryoichi:1
CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    username   varchar(32) NOT NULL UNIQUE,
    password   varchar(72) NOT NULL,
    role       varchar(16) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL
)
--rollback DROP TABLE users;