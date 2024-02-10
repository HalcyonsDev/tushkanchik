-- changeset: create-courses-users
-- author: halcyon

-- createTable: courses_users
CREATE TABLE courses_users(
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
)