--liquibase formatted sql

--changeset ryoichi:1
CREATE TABLE IF NOT EXISTS book
(
    id          SERIAL PRIMARY KEY,
    user_id     INTEGER REFERENCES users (id) ON DELETE CASCADE,
    title       VARCHAR(128) NOT NULL,
    description TEXT         NOT NULL,
    author      VARCHAR(64)  NOT NULL,
    path        VARCHAR(256) NOT NULL,
    visible     BOOLEAN      NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL
)
--rollback DROP TABLE book;