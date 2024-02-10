-- changeset: create-page
-- author: halcyon

-- createTable: pages
CREATE TABLE pages(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    course_id BIGINT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id)
)