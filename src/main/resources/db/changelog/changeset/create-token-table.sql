-- changeset: create-token
-- author: halcyon

-- createTable: tokens
CREATE TABLE tokens(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    value VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id)
)