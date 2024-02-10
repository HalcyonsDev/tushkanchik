-- changeset: create-course
-- author: halcyon

-- createTable: courses
CREATE TABLE courses(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id)
)