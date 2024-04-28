--liquibase formatted sql

--changeset ryoichi:1
CREATE TABLE IF NOT EXISTS favourite
(
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    book_id INTEGER REFERENCES book(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, book_id)
)
--rollback DROP TABLE favourite;