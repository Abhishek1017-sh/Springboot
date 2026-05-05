CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE volunteers (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    total_hours INT DEFAULT 0,
    rating NUMERIC(3, 2) DEFAULT 0.00
);

CREATE TABLE skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE volunteer_skills (
    volunteer_id BIGINT REFERENCES volunteers(user_id) ON DELETE CASCADE,
    skill_id BIGINT REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (volunteer_id, skill_id)
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL
);

CREATE TABLE task_skills (
    task_id BIGINT REFERENCES tasks(id) ON DELETE CASCADE,
    skill_id BIGINT REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, skill_id)
);

CREATE TABLE applications (
    id BIGSERIAL PRIMARY KEY,
    volunteer_id BIGINT REFERENCES volunteers(user_id) ON DELETE CASCADE,
    task_id BIGINT REFERENCES tasks(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(volunteer_id, task_id)
);

CREATE TABLE assignments (
    id BIGSERIAL PRIMARY KEY,
    volunteer_id BIGINT REFERENCES volunteers(user_id) ON DELETE CASCADE,
    task_id BIGINT REFERENCES tasks(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(volunteer_id, task_id)
);

CREATE TABLE attendance (
    id BIGSERIAL PRIMARY KEY,
    volunteer_id BIGINT REFERENCES volunteers(user_id) ON DELETE CASCADE,
    task_id BIGINT REFERENCES tasks(id) ON DELETE CASCADE,
    check_in TIMESTAMP,
    check_out TIMESTAMP,
    location VARCHAR(255),
    UNIQUE(volunteer_id, task_id)
);

CREATE TABLE badges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    criteria VARCHAR(255) NOT NULL
);

CREATE TABLE volunteer_badges (
    volunteer_id BIGINT REFERENCES volunteers(user_id) ON DELETE CASCADE,
    badge_id BIGINT REFERENCES badges(id) ON DELETE CASCADE,
    earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (volunteer_id, badge_id)
);

CREATE TABLE feedback (
    id BIGSERIAL PRIMARY KEY,
    reviewer_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    target_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
