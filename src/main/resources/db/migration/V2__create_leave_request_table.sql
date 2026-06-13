CREATE TABLE IF NOT EXISTS leave_request (
    id                      BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    request_id              VARCHAR(100)    NOT NULL,
    workflow_id             VARCHAR(150)    NOT NULL,
    employee_id             VARCHAR(100)    NOT NULL,
    leave_type              VARCHAR(50)     NOT NULL,
    start_date              DATE            NOT NULL,
    end_date                DATE            NOT NULL,
    reason                  TEXT            NULL,
    status                  VARCHAR(50)     NOT NULL,
    manager_approved_by     VARCHAR(100)    NULL,
    manager_approved_at     TIMESTAMPTZ     NULL,
    hr_approved_by          VARCHAR(100)    NULL,
    hr_approved_at          TIMESTAMPTZ     NULL,
    rejected_by             VARCHAR(100)    NULL,
    rejected_reason         TEXT            NULL,
    rejected_at             TIMESTAMPTZ     NULL,
    created_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_leave_request PRIMARY KEY (id),
    CONSTRAINT uq_leave_request_request_id UNIQUE (request_id),
    CONSTRAINT uq_leave_request_workflow_id UNIQUE (workflow_id)
);

CREATE INDEX IF NOT EXISTS idx_leave_request_employee_id ON leave_request(employee_id);
CREATE INDEX IF NOT EXISTS idx_leave_request_status ON leave_request(status);
CREATE INDEX IF NOT EXISTS idx_leave_request_created_at ON leave_request(created_at);
