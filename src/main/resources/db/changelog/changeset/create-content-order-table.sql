-- changeset: create-content-order
-- author: halcyon

-- createTable: content-orders
CREATE TABLE content_orders(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    page_id BIGINT NOT NULL,
    content_id BIGINT NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    content_order INT NOT NULL
)