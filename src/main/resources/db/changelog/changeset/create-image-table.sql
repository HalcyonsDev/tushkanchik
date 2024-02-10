-- changeset: create-image
-- author: halcyon

-- createTable: images
CREATE TABLE images(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    created_at TIMESTAMP NOT NULL,
    source VARCHAR(255) NOT NULL,
    content_order_id BIGINT,
    page_id BIGINT NOT NULL,
    FOREIGN KEY (page_id) REFERENCES pages(id)
)