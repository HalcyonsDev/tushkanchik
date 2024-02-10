-- changeset: create-text
-- author: halcyon

-- createTable: texts
CREATE TABLE texts(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    created_at TIMESTAMP NOT NULL,
    content VARCHAR(255) NOT NULL,
    content_order_id BIGINT,
    page_id BIGINT NOT NULL,
    FOREIGN KEY (page_id) REFERENCES pages(id)
)