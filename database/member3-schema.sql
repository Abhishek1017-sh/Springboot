CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    date DATE
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    event_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE task_skills (
    task_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (task_id, skill_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
    -- skill_id references skills table created by Member 2
);

CREATE TABLE applications (
    id SERIAL PRIMARY KEY,
    volunteer_id INT NOT NULL,
    task_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
    -- volunteer_id references volunteers table created by Member 2
);

CREATE TABLE assignments (
    id SERIAL PRIMARY KEY,
    volunteer_id INT NOT NULL,
    task_id INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
    -- volunteer_id references volunteers table created by Member 2
);
