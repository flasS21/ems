CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE employee_project (
    employee_id BIGINT NOT NULL REFERENCES employee(id),
    project_id BIGINT NOT NULL REFERENCES project(id),
    PRIMARY KEY (employee_id, project_id)
);

CREATE INDEX idx_employee_project_project_id ON employee_project(project_id);
